package fr.treeptik.cloudunit.cli.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.stereotype.Component;

import fr.treeptik.cloudunit.cli.commands.ShellStatusCommand;
import fr.treeptik.cloudunit.cli.exception.ManagerResponseException;
import fr.treeptik.cloudunit.cli.processor.InjectLogger;
import fr.treeptik.cloudunit.cli.rest.JsonConverter;
import fr.treeptik.cloudunit.cli.rest.RestUtils;
import fr.treeptik.cloudunit.model.Volume;

@Component
public class VolumeService {

	@InjectLogger
	private Logger log;

	@Autowired
	private AuthentificationUtils authentificationUtils;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private ShellStatusCommand statusCommand;

	/**
	 * @param name
	 * @return
	 */
	public String createVolume(String name) {
		try {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("name", name);
			restUtils.sendPostCommand(authentificationUtils.finalHost + "/volume",
					authentificationUtils.getMap(), parameters);
		} catch (ManagerResponseException e) {
			statusCommand.setExitStatut(ExitShellRequest.FATAL_EXIT.getExitCode());
			return ANSIConstants.ANSI_RED + e.getMessage() + ANSIConstants.ANSI_RESET;
		}
		statusCommand.setExitStatut(ExitShellRequest.NORMAL_EXIT.getExitCode());
		return name;
	}

	/**
	 * @return list of volumes as text
	 */
	public String displayVolumes() {
		String response;
		try {
			response = restUtils.sendGetCommand(authentificationUtils.finalHost + "/volume",
					authentificationUtils.getMap()).get("body");
		} catch (ManagerResponseException e) {
			statusCommand.setExitStatut(1);
			return ANSIConstants.ANSI_RED + e.getMessage() + ANSIConstants.ANSI_RESET;
		}
		String volumesAsTxt = MessageConverter.buildListVolumes(JsonConverter.getVolumes(response));
		statusCommand.setExitStatut(0);
		return volumesAsTxt;
	}

	/**
	 * @param name
	 * @return
	 */
	public String removeVolume(String name) {
		String response;
		if (authentificationUtils.getMap().isEmpty()) {
			statusCommand.setExitStatut(1);
			return ANSIConstants.ANSI_RED + "You are not connected to CloudUnit host! Please use connect command"
					+ ANSIConstants.ANSI_RESET;
		}
		try {
			response = restUtils.sendGetCommand(
					authentificationUtils.finalHost + "/volume",
					authentificationUtils.getMap()).get("body");
			List<Volume> volumes = JsonConverter.getVolumes(response);
			int id = -1;
			for(Volume var : volumes)
				if(var.getName().equals(name))
					id = var.getId();
			restUtils.sendDeleteCommand(authentificationUtils.finalHost + "/volume/" + id,
					authentificationUtils.getMap());
		} catch (ManagerResponseException e) {
			statusCommand.setExitStatut(1);
			return ANSIConstants.ANSI_RED + false + ANSIConstants.ANSI_RESET;
		}
		statusCommand.setExitStatut(0);
		return "true";
	}
}
