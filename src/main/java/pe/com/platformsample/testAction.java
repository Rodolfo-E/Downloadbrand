package pe.com.platformsample;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.repo.jscript.ScriptNode;
import org.alfresco.rest.framework.webscripts.ResourceWebScriptHelper;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.WebScriptResponseImpl;
import org.springframework.extensions.webscripts.WebScriptStatus;

import io.netty.handler.codec.http.HttpResponse;

public class testAction extends ActionExecuterAbstractBase {
	private static Log logger = LogFactory.getLog(testAction.class);

	public static final String PARAM_X = "x";
	public static final String PARAM_Y = "y";
	public static final String PARAM_WITH = "width";
	public static final String PARAM_HEIGHT = "height";
	public static final String PARAM_ALLPAGES = "allpages";
	private ServiceRegistry serviceRegistry;
	private Downloadprimero download;
	private WebScriptResponse webScriptResponse;
	private WebScriptRequest webScriptRequest;


    private ScriptNode scriptNode;
	

	public void setDownload(Downloadprimero download) {
		this.download = download;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		 paramList.add(new ParameterDefinitionImpl(PARAM_X, DataTypeDefinition.TEXT, true, getParamDisplayLabel(PARAM_X)));
		    paramList.add(new ParameterDefinitionImpl(PARAM_Y, DataTypeDefinition.TEXT, true, getParamDisplayLabel(PARAM_Y)));
		    paramList.add(new ParameterDefinitionImpl(PARAM_WITH, DataTypeDefinition.TEXT, true, getParamDisplayLabel(PARAM_WITH)));
		    paramList.add(new ParameterDefinitionImpl(PARAM_HEIGHT, DataTypeDefinition.TEXT, true, getParamDisplayLabel(PARAM_HEIGHT)));
		    paramList.add(new ParameterDefinitionImpl(PARAM_ALLPAGES, DataTypeDefinition.TEXT, true, getParamDisplayLabel(PARAM_ALLPAGES)));

	}

	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		String x = (String) action.getParameterValue(PARAM_X);
		String y = (String) action.getParameterValue(PARAM_Y);
		String width = (String) action.getParameterValue(PARAM_WITH);
		String height = (String) action.getParameterValue(PARAM_HEIGHT);
		String allpages = (String) action.getParameterValue(PARAM_ALLPAGES);
		System.out.println(x + " - " + y + " - " + width + " - " + height);
		System.out.println(actionedUponNodeRef);
		logger.debug("entro");
		System.out.println("entro");
		
		try {
		String web="http://192.168.18.47:8080/alfresco/s/sample/download?nodeRef="+actionedUponNodeRef+"&x="+x+"&y="+y+"&width="+width+"&height="+height+"&chkpages="+allpages;	
		System.out.println("todos");
		 URL url = new URL(web);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         // Agrega credenciales si es necesario
         String username = "admin";
         String password = "admin";
         String userPassword = username + ":" + password;
         String encoding = java.util.Base64.getEncoder().encodeToString(userPassword.getBytes());
         conn.setRequestProperty("Authorization", "Basic " + encoding);
         System.out.println("conexion: "+conn);
         conn.setRequestMethod("GET");
		 int statusCode = conn.getResponseCode();
		 
		 System.out.println("status"+statusCode);

         if (statusCode == 200) {
        	 System.out.println("se descargo documento");
             // La solicitud se ejecutó con éxito.
             BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             String inputLine;
             StringBuffer response = new StringBuffer();

             while ((inputLine = in.readLine()) != null) {
                 response.append(inputLine);
             }
             in.close();

             // Procesa la respuesta del WebScript aquí.
         } else {
        	 System.out.println("Error al descargar el documento");
             // La solicitud al WebScript falló.
             // Realiza el manejo de errores adecuado.
         }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*if (serviceRegistry.getNodeService().exists(actionedUponNodeRef)) {

			try {
				String x = (String) action.getParameterValue(PARAM_X);
				String y = (String) action.getParameterValue(PARAM_Y);
				String width = (String) action.getParameterValue(PARAM_WITH);
				String height = (String) action.getParameterValue(PARAM_HEIGHT);
				String allpages = (String) action.getParameterValue(PARAM_ALLPAGES);
				System.out.println(x + " - " + y + " - " + width + " - " + height);
				System.out.println(actionedUponNodeRef);
				download.download(actionedUponNodeRef, x, y, width, height, allpages);
				download.execute(webScriptRequest, webScriptResponse);

				logger.debug("entro " + x + " - " + y + " - " + width + " - " + height);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());

			}

		}*/
	}

}
