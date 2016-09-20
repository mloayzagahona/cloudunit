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

package fr.treeptik.cloudunit.servers;

import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.initializer.CloudUnitApplicationContext;
import fr.treeptik.cloudunit.model.User;
import fr.treeptik.cloudunit.service.UserService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.servlet.Filter;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by nicolas on 08/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CloudUnitApplicationContext.class, MockServletContext.class})
@ActiveProfiles("integration")
public abstract class AbstractApplicationControllerTestIT {

    protected String release;

    private final Logger logger = LoggerFactory.getLogger(AbstractApplicationControllerTestIT.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Inject
    private AuthenticationManager authenticationManager;

    @Autowired
    private Filter springSecurityFilterChain;

    @Inject
    private UserService userService;

    private MockHttpSession session;

    private static String applicationName;

    @Before
    public void setup() throws Exception {
        logger.info("setup");
        applicationName = "app" + new Random().nextInt(100000);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();

        User user = null;
        try {
            user = userService.findByLogin("johndoe");
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }

        Authentication authentication = null;
        if (user != null) {
            authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        }
        Authentication result = authenticationManager.authenticate(authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(result);
        session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    @After
    public void teardown() throws Exception {
        logger.info("teardown");
        SecurityContextHolder.clearContext();
        session.invalidate();
    }

    /**
     * We cannot create an application with an empty name.
     *
     * @throws Exception
     */
    @Test
    public void test_failCreateEmptyNameApplication()
        throws Exception {
        logger.info("Create application with an empty name");
        final String jsonString = "{\"applicationName\":\"" + "" + "\", \"serverName\":\"" + release + "\"}";
        ResultActions resultats =
            this.mockMvc.perform(post("/application")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
        resultats.andExpect(status().is4xxClientError());
    }

    /**
     * We cannot create an application with an wrong syntax name.
     *
     * @throws Exception
     */
    @Test(timeout = 30000)
    public void test_failCreateWrongNameApplication()
        throws Exception {
        logger.info("Create application with a wrong syntax name");
        ResultActions resultats = createApplication("            ");
        resultats.andExpect(status().is4xxClientError());
    }


    @Test
    public void test_createAccentNameApplication()
            throws Exception {

        String accentName = "aeeioù";
        String deAccentName = "aeeiou";

        ResultActions resultats = createApplication(accentName);
        resultats.andExpect(status().isOk());

        resultats = deleteApplication(deAccentName);
        resultats.andExpect(status().isOk());

    }


    @Test()
    public void test_startStopStartApplicationTest()
            throws Exception {

        ResultActions resultats = createApplication(applicationName);
        resultats.andExpect(status().isOk());

        resultats = startApplication();
        resultats.andExpect(status().isOk());

        resultats = stopApplication();
        resultats.andExpect(status().isOk());

        resultats = startApplication();
        resultats.andExpect(status().isOk());

        resultats = deleteApplication(applicationName);
        resultats.andExpect(status().isOk());

    }

    @Test()
    public void test_changeJvmMemorySizeApplicationTest()
            throws Exception {

        ResultActions resultats = createApplication(applicationName);
        resultats.andExpect(status().isOk());

        resultats = changeJVMMemory(applicationName, "1024");
        resultats.andExpect(status().isOk());

        resultats = getApplicationInformations(applicationName);
        resultats.andExpect(jsonPath("$.server.jvmMemory").value(1024));

        deleteApplication(applicationName);

    }

    @Test(timeout = 30000)
    public void test_changeInvalidJvmMemorySizeApplicationTest()
            throws Exception {

        ResultActions resultats = createApplication(applicationName);
        resultats.andExpect(status().isOk());

        logger.info("Change JVM Memory size with an incorrect value : number not allowed");
        resultats = changeJVMMemory(applicationName, "666");
        resultats.andExpect(status().is4xxClientError());

        logger.info("Change JVM Memory size with an empty value");
        resultats = changeJVMMemory(applicationName, "");
        resultats.andExpect(status().is4xxClientError());

        resultats = deleteApplication(applicationName);
        resultats.andExpect(status().isOk());
    }

    @Test(timeout = 60000)
    public void test_changeJvmOptionsApplicationTest()
            throws Exception {

        ResultActions resultats = createApplication(applicationName);
        resultats.andExpect(status().isOk());

        logger.info("Change JVM Options !");
        resultats = changeJVMOptions(applicationName, "-Dkey1=value1");
        resultats.andExpect(status().isOk());

        resultats = getApplicationInformations(applicationName);
        resultats.andExpect(jsonPath("$.server.jvmMemory").value(512)).andExpect(jsonPath(
                "$.server.jvmRelease").value("jdk1.8.0_25")).andExpect(jsonPath(
                "$.server.jvmOptions").value("-Dkey1=value1"));

        resultats = deleteApplication(applicationName);
        resultats.andExpect(status().isOk());
    }

    @Test(timeout = 30000)
    public void test_changeFailWithXmsJvmOptionsApplicationTest()
            throws Exception {

        ResultActions resultats = createApplication(applicationName);
        resultats.andExpect(status().isOk());

        logger.info("Change JVM With Xms : not allowed");
        resultats = changeJVMOptions(applicationName, "-Xms=512m");
        resultats.andExpect(status().is4xxClientError());

        resultats = deleteApplication(applicationName);
        resultats.andExpect(status().isOk());
    }

    @Test()
    public void test_openAPort()
            throws Exception {

        ResultActions resultats = createApplication(applicationName);
        resultats.andExpect(status().isOk());

        logger.info("Open custom ports !");
        resultats = openPort(applicationName, "6115");
        resultats.andExpect(status().isOk());

        logger.info("Close custom ports !");
        resultats = closePort(applicationName, "6115");
        resultats.andExpect(status().isOk());

        resultats = deleteApplication(applicationName);
        resultats.andExpect(status().isOk());

    }

    private ResultActions stopApplication() throws Exception {
        logger.info("Stop the application : " + applicationName);
        final String jsonString = "{\"applicationName\":\"" + applicationName + "\"}";
        ResultActions resultats = mockMvc.perform(post("/application/stop").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return resultats;
    }

    private ResultActions startApplication() throws Exception {
        logger.info("Start the application : " + applicationName);
        final String jsonString = "{\"applicationName\":\"" + applicationName + "\"}";
        ResultActions resultats = mockMvc.perform(post("/application/start").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return resultats;
    }

    private ResultActions deleteApplication(String applicationName) throws Exception {
        logger.info("Delete application : " + applicationName);
        ResultActions resultats =
                mockMvc.perform(delete("/application/" + applicationName).session(session).contentType(MediaType.APPLICATION_JSON));
        return resultats;
    }

    private ResultActions createApplication(String accentName) throws Exception {
        logger.info("Create application with accent name " + accentName);
        final String jsonString = "{\"applicationName\":\"" + accentName + "\", \"serverName\":\"" + release + "\"}";
        ResultActions resultats =
                this.mockMvc.perform(post("/application").session(session).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString));
        return resultats;
    }

    private ResultActions getApplicationInformations(String name) throws Exception {
        return mockMvc.perform(get("/application/" + applicationName).session(session).contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions changeJVMMemory(String name, String jvmMemory) throws Exception {
        logger.info("Change JVM Memory !");
        String jsonString =
                "{\"applicationName\":\"" + name
                        + "\",\"jvmMemory\":\"" + jvmMemory + "\",\"jvmOptions\":\"\",\"jvmRelease\":\"jdk1.8.0_25\",\"location\":\"webui\"}";
        ResultActions resultats =
                this.mockMvc.perform(put("/server/configuration/jvm").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return resultats;
    }

    private ResultActions changeJVMOptions(String name, String options) throws Exception {
        String jsonString =
                "{\"applicationName\":\"" + name
                        + "\",\"jvmMemory\":\"512\",\"jvmOptions\":\""+ options + "\",\"jvmRelease\":\"jdk1.8.0_25\"}";
        ResultActions resultats =
                mockMvc.perform(put("/server/configuration/jvm").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return resultats;
    }

    private ResultActions openPort(String name, String port) throws Exception {
        String jsonString =
                "{\"applicationName\":\"" + name
                        + "\",\"portToOpen\":\"" + port + "\",\"alias\":\"access6115\"}";
        ResultActions resultats =
                this.mockMvc.perform(post("/server/ports/open").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return resultats;
    }

    private ResultActions closePort(String name, String port) throws Exception {
        String jsonString =
                "{\"applicationName\":\"" + name
                        + "\",\"portToOpen\":\"" + port + "\",\"alias\":\"access6115\"}";
        ResultActions resultats =
                this.mockMvc.perform(post("/server/ports/close").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        return resultats;
    }

}