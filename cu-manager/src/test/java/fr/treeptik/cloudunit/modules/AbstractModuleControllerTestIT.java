/*
 * LICENCE : CloudUnit is available under the Affero Gnu Public License GPL V3 : https://www.gnu.org/licenses/agpl-3.0.html
 *     but CloudUnit is licensed too under a standard commercial license.
 *     Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
 *     If you are not sure whether the GPL is right for you,
 *     you can always test our software under the GPL and inspect the source code before you contact us
 *     about purchasing a commercial license.
 *
 *     LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
 *     or promote products derived from this project without prior written permission from Treeptik.
 *     Products or services derived from this software may not be called "CloudUnit"
 *     nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
 *     For any questions, contact us : contact@treeptik.fr
 */

package fr.treeptik.cloudunit.modules;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.Filter;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.treeptik.cloudunit.dto.EnvUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.treeptik.cloudunit.dto.ModuleResource;
import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.initializer.CloudUnitApplicationContext;
import fr.treeptik.cloudunit.model.User;
import fr.treeptik.cloudunit.service.UserService;
import fr.treeptik.cloudunit.utils.SpyMatcherDecorator;
import junit.framework.TestCase;

/**
 * Tests for Module lifecycle
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
    CloudUnitApplicationContext.class,
    MockServletContext.class
})
@ActiveProfiles("integration")
public abstract class AbstractModuleControllerTestIT extends TestCase {

    protected final Logger logger = LoggerFactory
        .getLogger(AbstractModuleControllerTestIT.class);

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private AuthenticationManager authenticationManager;

    @Autowired
    private Filter springSecurityFilterChain;

    @Inject
    private UserService userService;

    @Value("${cloudunit.instance.name}")
    private String cuInstanceName;

    @Value("${ip.box.vagrant}")
    protected String ipVagrantBox;

    protected MockHttpSession session;

    protected static String applicationName;

    @Value("${suffix.cloudunit.io}")
    private String domainSuffix;

    @Value("#{systemEnvironment['CU_SUB_DOMAIN']}")
    private String subdomain;

    protected String server;
    protected String module;
    protected String managerPrefix;
    protected String managerSuffix;
    protected String managerPageContent;

    @BeforeClass
    public static void initEnv() {
        applicationName = "app" + new Random().nextInt(100000);
    }

    @Before
    public void setup() throws Exception {
        logger.info("setup");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(springSecurityFilterChain)
            .build();

        User user = null;
        try {
            user = userService.findByLogin("johndoe");
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }

        assert user != null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        Authentication result = authenticationManager.authenticate(authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(result);
        session = new MockHttpSession();
        String secContextAttr = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
        session.setAttribute(secContextAttr,
            securityContext);

        // create an application server
        createApplication(applicationName, server);
    }

    @After
    public void teardown() throws Exception {
        logger.info("teardown");

        logger.info("Delete application : " + applicationName);

        deleteApplication(applicationName);

        SecurityContextHolder.clearContext();
        session.invalidate();
    }

    @Test
    public void test00_FailToAddModuleBecauseBadAppName() throws Exception {
        logger.info("Cannot add a module because application name missing");
        ResultActions resultats = createModule("", module);
        resultats.andExpect(status().is4xxClientError());
    }

    @Test
    public void test01_FailToAddModuleBecauseAppNonExist() throws Exception {
        logger.info("Cannot add a module because application name missing");
        ResultActions resultats = createModule("UFO", module);
        resultats.andExpect(status().is4xxClientError());
    }

    @Test
    public void test02_FailToAddModuleBecauseModuleEmpty() throws Exception {
        logger.info("Cannot add a module because module name empty");
        ResultActions resultats = createModule(applicationName, "");
        resultats.andExpect(status().is4xxClientError());
    }

    @Test
    public void test03_FailToAddModuleBecauseModuleNonExisting() throws Exception {
        logger.info("Cannot add a module because module name empty");
        ResultActions resultats = createModule(applicationName, "UFO");
        resultats.andExpect(status().is5xxServerError());
    }

    @Test
    public void test10_CreateServerThenAddModule() throws Exception {
        logger.info("Create an application, add a " + module + " module and delete it");

        // verify if app exists
        ResultActions resultats = getApplicationInformations(applicationName);
        resultats.andExpect(jsonPath("name").value(applicationName.toLowerCase()));

        // add a module
        resultats = createModule(applicationName, module);
        resultats.andExpect(status().isOk());

        // Expected values
        String genericModule = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module;

        // get the detail of the applications to verify modules addition
        resultats = getApplicationInformations(applicationName).andDo(print());
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0].status").value("START"))
            .andExpect(jsonPath("$.modules[0].name").value(genericModule));

        // remove a module
        resultats = deleteModule(applicationName, genericModule);
        resultats.andExpect(status().isOk());

        // get the detail of the applications to verify modules addition
        resultats = getApplicationInformations(applicationName).andDo(print());
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0]").doesNotExist());
    }

    @Test
    public void test20_FailCreateServerThenAddTwoModule() throws Exception {
        logger.info("Create an application, add two " + module + " modules, stop them then delete all");

        // verify if app exists
        ResultActions resultats = getApplicationInformations(applicationName);
        resultats.andExpect(jsonPath("name").value(applicationName.toLowerCase()));

        // add a first module
        resultats = createModule(applicationName, module);
        resultats.andExpect(status().isOk());

        // Expected values
        String module1 = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module;

        // get the detail of the applications to verify modules addition
        resultats = getApplicationInformations(applicationName);
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0].status").value("START"))
            .andExpect(jsonPath("$.modules[0].name").value(module1));

        // add a second module
        resultats = createModule(applicationName, module);
        resultats.andExpect(status().is4xxClientError());
    }

    @Test
    public void test30_AddModuleThenRestart() throws Exception {
        logger.info("Create an application, add a " + module + " modules, restart");

        // verify if app exists
        ResultActions resultats = getApplicationInformations(applicationName);
        resultats.andExpect(jsonPath("name").value(applicationName.toLowerCase()));

        // add a first module
        resultats = createModule(applicationName, module);
        resultats.andDo(print());
        resultats.andExpect(status().isOk());

        // Expected values
        String module1 = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module;

        // Stop the application
        resultats = stopApplication(applicationName);
        resultats.andDo(print());
        resultats.andExpect(status().isOk());

        // Start the application
        resultats = startApplication(applicationName);
        resultats.andDo(print());
        resultats.andExpect(status().isOk());

        // get the detail of the applications to verify modules addition
        resultats = getApplicationInformations(applicationName);
        resultats.andDo(print());
        resultats
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules[0].status").value("START"))
                .andExpect(jsonPath("$.modules[0].name").value(module1));
    }

    public ResultActions requestPublishPort(Integer id) throws Exception {
        ModuleResource request = ModuleResource.of()
                .withPublishPort(true)
                .build();
        return updateModule(request, id)
            .andDo(print());
    }

    /*public ResultActions requestAddModule() throws Exception {
        String jsonString = "{\"applicationName\":\"" + applicationName + "\", \"imageName\":\"" + module + "\"}";
        return createModule(applicationName, module)
            .andDo(print());
    }

    public ResultActions requestApplication() throws Exception {
        return getApplicationInformations(applicationName)
            .andDo(print());
    }*/

    @Test
    public void test_PublishPort() throws Exception {
        logger.info("Publish module port for external access");

        createModule(applicationName, module)
        	.andExpect(status().isOk());

        SpyMatcherDecorator<Integer> responseModuleIdSpy = new SpyMatcherDecorator<>();
        SpyMatcherDecorator<String> forwardedPort = new SpyMatcherDecorator<>();

        getApplicationInformations(applicationName)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0].id", responseModuleIdSpy));

        Integer moduleId = responseModuleIdSpy.getMatchedValue();
        requestPublishPort(moduleId)
            .andExpect(status().isOk());

        getApplicationInformations(applicationName)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules[0].forwardedPort", forwardedPort));

        checkConnectionDatabase(forwardedPort.getMatchedValue());
    }

    protected abstract void checkConnectionDatabase(String forwardedPort);

    private String getContainerName() {
        return "int-johndoe-"+applicationName+"-"+module;
    }

    /**
     * Inner class to check relational database connection
     *
     */
    public class CheckDatabaseConnection {

        public void invoke(String forwardedPort, String keyUser, String keyPassword,
                           String keyDB, String driver, String jdbcUrlPrefix) {

            String user = null;
            String password = null;
            String database = null;
            Connection connection = null;

            try {
                String urlToCall = "/application/" + applicationName + "/container/"+getContainerName()+"/env";
                ResultActions resultats = mockMvc.perform(get(urlToCall).session(session).contentType(MediaType.APPLICATION_JSON));
                String contentResult = resultats.andReturn().getResponse().getContentAsString();
                List<EnvUnit> envs = objectMapper.readValue(contentResult, new TypeReference<List<EnvUnit>>(){});
                user = envs.stream().filter(e -> e.getKey().equals(keyUser)).findFirst().orElseThrow(() -> new RuntimeException("Missing " + keyUser)).getValue();
                password = envs.stream().filter(e -> e.getKey().equals(keyPassword)).findFirst().orElseThrow(() -> new RuntimeException("Missing " + keyPassword)).getValue();
                database = envs.stream().filter(e -> e.getKey().equals(keyDB)).findFirst().orElseThrow(() -> new RuntimeException("Missing " + keyDB)).getValue();
                //String urlJDBC = "jdbc:postgresql://"+ipVagrantBox+":"+forwardedPort+"/" + database;
                String jdbcUrl = jdbcUrlPrefix+ipVagrantBox+":"+forwardedPort+"/" + database;
                //Class.forName("org.postgresql.Driver");
                Class.forName(driver);
                int counter = 0;
                boolean isRight = false;
                while(counter++ < 5 && !isRight) {
                    try {
                        connection = DriverManager.getConnection(jdbcUrl, user, password);
                        isRight = connection.isValid(1000);
                    } catch (Exception e) {
                    }
                    Thread.sleep(1000);
                }
                if (counter >= 5) throw new RuntimeException("Cannot connect to database : " + jdbcUrl);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            } finally {
                try {
                    if (connection != null) { connection.close(); }
                } catch (Exception ignore){}
            }
        }
    }

    private ResultActions createApplication(String name, String server) throws Exception {
        String jsonString = "{\"applicationName\":\"" + name + "\", \"serverName\":\"" + server + "\"}";
        ResultActions actions = mockMvc.perform(post("/application")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
        return actions;
    }

    private void deleteApplication(String name) throws Exception {
        mockMvc.perform(delete("/application/" + name)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ResultActions getApplicationInformations(String name) throws Exception {
        ResultActions actions = mockMvc.perform(get("/application/" + name)
                .session(session).contentType(MediaType.APPLICATION_JSON));
        return actions;
    }

    private ResultActions startApplication(String name) throws Exception {
        String jsonString = "{\"applicationName\":\"" + name + "\"}";
        ResultActions actions = mockMvc.perform(post("/application/start").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return actions;
    }

    private ResultActions stopApplication(String name) throws Exception {
        String jsonString = "{\"applicationName\":\"" + name + "\"}";
        ResultActions actions = mockMvc.perform(post("/application/stop").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return actions;
    }

    private ResultActions createModule(String name, String image) throws Exception {
        String jsonString = "{\"applicationName\":\"" + name + "\", \"imageName\":\"" + image + "\"}";
        ResultActions actions = mockMvc.perform(post("/module")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
        return actions;
    }

    private ResultActions deleteModule(String name, String module) throws Exception {
        ResultActions actions = mockMvc.perform(delete("/module/" + name + "/" + module)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));
        return actions;
    }

    private ResultActions updateModule(ModuleResource request, Integer id) throws Exception {
        String jsonString = objectMapper.writeValueAsString(request);
        return mockMvc.perform(put("/module/" + id)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
    }

}