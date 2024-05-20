package pe.com.platformsample;

import java.util.HashMap;
import java.util.Map;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class MostrarForm extends DeclarativeWebScript {

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		Map<String, Object> model = new HashMap<String, Object>();
		String node=req.getParameter("nodeRef");
		System.out.println("node "+node);
		model.put("node", node);
		return model;
	}
	
	

}
