/**
 * Copyright (C) 2017 Alfresco Software Limited.
 * <p/>
 * This file is part of the Alfresco SDK project.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pe.com.platformsample;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * A demonstration Java controller for the Hello World Web Script.
 *
 * @author martin.bergljung@alfresco.com
 * @since 2.1.0
 */
public class Downloadprimero extends AbstractWebScript {
	private static Log logger = LogFactory.getLog(Downloadprimero.class);
	private static final StoreRef STORE_REF = StoreRef.STORE_REF_WORKSPACE_SPACESSTORE;
	private static final String BASE_PATH = "workspace/SpacesStore/Company Home";
	private ServiceRegistry serviceRegistry;
	private NodeService nodeService;
	private Repository repository;
	

	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	


	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res) {
		// Map<String, Object> model = new HashMap<String, Object>();
		logger.debug("Debug inicio");
		System.out.println("los mejores 3");
		String uuid = req.getParameter("nodeRef");
		System.out.println("uuid " + uuid);
	//	NodeRef attachmentNodeRef = new NodeRef("workspace://SpacesStore/" + uuid);
		NodeRef attachmentNodeRef = new NodeRef(uuid);

		if (attachmentNodeRef != null) {
			// Obtiene el ContentReader del archivo
			ContentReader contentReader = serviceRegistry.getContentService().getReader(attachmentNodeRef,
					ContentModel.PROP_CONTENT);
			System.out.println("el myme" + contentReader.getMimetype());
			String name = (String) nodeService.getProperty(attachmentNodeRef, ContentModel.PROP_NAME);
			try {
				// Carga el documento PDF
				PDDocument document = PDDocument.load(contentReader.getContentInputStream());

				// Obtén la imagen de la marca de agua desde Alfresco Share

				// NodeRef watermarkNodeRef = new
				// NodeRef("workspace://SpacesStore/1e98f2de-ac1b-4a2e-9343-b78c32608db5");
				String namePATH = "MarcaAgua/PDF/sello.png/";
				NodeRef watermarkNodeRef = getNodeByPath(namePATH);
				ContentReader watermarkReader = serviceRegistry.getContentService().getReader(watermarkNodeRef,
						ContentModel.PROP_CONTENT);

				// Obtén los bytes de la imagen de marca de agua desde Alfresco
				InputStream watermarkInputStream = watermarkReader.getContentInputStream();
				ByteArrayOutputStream watermarkByteArrayOutputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = watermarkInputStream.read(buffer)) != -1) {
					watermarkByteArrayOutputStream.write(buffer, 0, bytesRead);
				}
				byte[] watermarkBytes = watermarkByteArrayOutputStream.toByteArray();

				// Crea un objeto PDImageXObject a partir de los bytes de la imagen de marca de
				// agua
				PDImageXObject watermarkImage = PDImageXObject.createFromByteArray(document, watermarkBytes,
						"watermark");
				
				String t=req.getParameter("trans");
				
				// Determina el nivel de transparencia deseado (0.0 a 1.0)
				float trans=(t !=null && !t.isEmpty())?  Float.parseFloat(t+"f"):0.5f;
				PDExtendedGraphicsState transparency = transparencia(trans);

				// Crea un objeto PDExtendedGraphicsState para definir la transparencia

				boolean allpages = Boolean.parseBoolean(req.getParameter("chkpages"));
				//boolean allpages = Boolean.parseBoolean(this.allpages);

				// for (PDPage page : document.getPages()) {
				for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
					PDPage page = document.getPage(pageIndex);

					if (allpages || pageIndex == 0) {

						// Crear un nuevo contenido en la página
						PDPageContentStream contentStream = new PDPageContentStream(document, page,
								PDPageContentStream.AppendMode.APPEND, true, true);

						// Configurar la posición y tamaño de la marca de agua
					
						String xParam = req.getParameter("x");
						String yParam = req.getParameter("y");
						String widthParam = req.getParameter("width");
						String heightParam = req.getParameter("height");
						/*String xParam = this.x;
						String yParam = this.y;
						String widthParam = this.with;
						String heightParam = this.height;*/
						// Verificar y asignar valores por defecto si los parámetros están ausentes o son nulos
						float x = (xParam != null && !xParam.isEmpty()) ? Float.parseFloat(xParam) : 230;
						float y = (yParam != null && !yParam.isEmpty()) ? Float.parseFloat(yParam) : 350;
						float width = (widthParam != null && !widthParam.isEmpty()) ? Float.parseFloat(widthParam) : 150;
						float height = (heightParam != null && !heightParam.isEmpty()) ? Float.parseFloat(heightParam) : 150;

					    
						// Aplicar la transparencia al contenido de la página
						contentStream.setGraphicsStateParameters(transparency);

						// Dibujar la imagen de marca de agua en la página
						contentStream.drawImage(watermarkImage, x, y, width, height);
						contentStream.close();

					}
				}

				// Configura la respuesta HTTP

				res.setContentType(contentReader.getMimetype());
				res.setContentEncoding(contentReader.getEncoding());
				res.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
				res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				res.setHeader("Pragma", "public");
				res.setHeader("Expires", "0");

				// Escribe el contenido del archivo en la respuesta HTTP
				OutputStream outputStream = res.getOutputStream();
				// contentReader.getContent(outputStream);
				document.save(outputStream);
				outputStream.flush();
				outputStream.close();
				document.close();
				/*
				 * res.getWriter().
				 * write("<script>alert('Se descargo con exito el archivo')</script>");
				 * res.getWriter().flush();
				 */

			} catch (Exception e) {

				System.out.println("Error al descargar el archivo con marca de agua." + e);
				System.out.println(e.getMessage());
			}
		} else {

			System.out.println("Archivo no encontrado.");

		}

	}

	public PDExtendedGraphicsState transparencia(float trans) {
		// Determina el nivel de transparencia deseado (0.0 a 1.0)
	//	float transparency = 0.5f; // Por ejemplo, 0.5 para 50% de transparencia
		PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
		graphicsState.setStrokingAlphaConstant(trans);
		graphicsState.setNonStrokingAlphaConstant(trans);
		return graphicsState;

	}

	public NodeRef getNodeByPath(String nodePath) {
		if (isBlank(nodePath))
			return null;

		nodePath = BASE_PATH + "/" + nodePath;
		return repository.findNodeRef("path", formatPath(nodePath).split("/"));
	}

	public static boolean isBlank(String ptext) {
		return ptext == null || ptext.trim().length() == 0 || ptext.trim().toLowerCase().equals("null");
	}

	public String formatPath(String path) {
		if (path.trim().startsWith("/"))
			path = path.trim().substring(1, path.length());

		if (path.trim().endsWith("/"))
			path = path.trim().substring(0, path.length() - 1);

		return path;
	}

}