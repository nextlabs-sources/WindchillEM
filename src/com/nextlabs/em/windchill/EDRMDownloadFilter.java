package com.nextlabs.em.windchill;

import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

import com.nextlabs.EntitlementManagerContext;

public class EDRMDownloadFilter implements Filter {
	private Logger logger = Logger.getLogger(EDRMDownloadFilter.class);

	public class CharResponseWrapper extends HttpServletResponseWrapper {
		private CharArrayWriter output;

		public String toString() {
			return output.toString();
		}

		public CharResponseWrapper(HttpServletResponse response) {
			super(response);
			output = new CharArrayWriter();
		}

		public PrintWriter getWriter() {
			return new PrintWriter(output);
		}
	}

	@Override
	public void destroy() {
		logger.info("EDRMDownloadFilter destroyed");

	}

	public void processfile(File localFile) {
		logger.info("EDRMDownloadFilter  localFile"
				+ localFile.getAbsolutePath());
		logger.info("EDRMDownloadFilter  localFile" + localFile.getName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		long lStartTime = System.currentTimeMillis();
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		/*
		 * if (response.getCharacterEncoding() == null) {
		 * response.setCharacterEncoding("UTF-8"); // Or whatever default. // }
		 */

		HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(
				(HttpServletResponse) response);
		responseCopier.setCharacterEncoding("ISO-8859-1");
		// HttpServletResponse httpServletResponse = (HttpServletResponse)
		// response;
		// PrintWriter out = httpServletResponse.getWriter();
		logger.info("EDRMDownloadFilter doFilter start at " + lStartTime);
		logger.info("EDRMDownloadFilter Filter Request url "
				+ httpServletRequest.getRequestURI());
		try {
			chain.doFilter(httpServletRequest, responseCopier);
			logger.info("EDRMDownloadFilter Filter response content type "
					+ responseCopier.getContentType());
			logger.info("EDRMDownloadFilter Filter getCharacterEncoding "
					+ responseCopier.getCharacterEncoding());
			responseCopier.flushBuffer();

			/*
			 * emCtx.log(LogLevel.INFO,
			 * "EDRMDownloadFilter Filter response inside document_contents.zip "
			 * );
			 * 
			 * emCtx.log(LogLevel.INFO,
			 * "EDRMDownloadFilter Filter response inside try block "); File
			 * file = new File(
			 * "C:\\Users\\abraham.lincoln\\Desktop\\document_contents.zip");
			 * emCtx.log(LogLevel.INFO,
			 * "EDRMDownloadFilter Filter after file declaration ");
			 */

			/*
			 * emCtx.log(LogLevel.INFO,
			 * "EDRMDownloadFilter httpServletResponse length " +
			 * wrapper.toString().length());
			 */
			/*
			 * if (wrapper.toString().length() > 100) emCtx.log(LogLevel.INFO,
			 * "EDRMDownloadFilter httpServletResponse " +
			 * wrapper.toString().substring(0, 100)); else emCtx.log(
			 * LogLevel.INFO, "EDRMDownloadFilter httpServletResponse " +
			 * wrapper.toString());
			 */
			/*
			 * httpServletResponse.setContentLength((int) file.length());
			 * 
			 * // forces download String headerKey = "Content-Disposition";
			 * String headerValue = String.format("attachment; filename=\"%s\"",
			 * file.getName()); httpServletResponse.setHeader(headerKey,
			 * headerValue); emCtx.log(LogLevel.INFO,
			 * "EDRMDownloadFilter Header section completed "); // obtains
			 * response's output stream OutputStream outStream =
			 * response.getOutputStream();
			 * 
			 * byte[] buffer = new byte[4096]; int bytesRead = -1;
			 * 
			 * while ((bytesRead = fin.read(buffer)) != -1) {
			 * outStream.write(buffer, 0, bytesRead); }
			 * 
			 * fin.close(); outStream.close();
			 */

		} catch (Exception ex) {
			logger.info("EDRMDownloadFilter Error " + ex.getMessage());
		} finally {
			byte[] copy = responseCopier.getCopy();

			String path = new File(".").getCanonicalPath()
					+ "\\document_contents.zip";
			String extractTo = new File(".").getCanonicalPath();
			// File zipFile = new File(path);

			logger.info("EDRMDownloadFilter path " + path);
			// FileOutputStream out = new FileOutputStream(zipFile);

			try {
				ZipInputStream zipStream = new ZipInputStream(
						new ByteArrayInputStream(copy, 15, (copy.length - 15)));

				logger.info("EDRMDownloadFilter inside zipstream "
						+ zipStream.available());
				logger.info("EDRMDownloadFilter inside zipstream "
						+ Byte.toString(copy[15]));
				ZipEntry entry = null;
				while ((entry = zipStream.getNextEntry()) != null) {
					logger.info("EDRMDownloadFilter inside zipstream first entry");
					String entryName = entry.getName();
					logger.info("EDRMDownloadFilter inside zipstream first entry name "
							+ entryName);
					FileOutputStream fout = new FileOutputStream(extractTo
							+ "/" + entryName);
					logger.info("EDRMDownloadFilter inside zipstream first entry size "
							+ entry.getSize());
					logger.info("EDRMDownloadFilter inside zipstream first entry method "
							+ entry.getMethod());

					logger.info("EDRMDownloadFilter inside zipstream first entry method "
							+ responseCopier.getCharacterEncoding());
					byte[] byteBuff = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = zipStream.read(byteBuff)) != -1) {
						fout.write(byteBuff, 0, bytesRead);
					}

					fout.close();
					zipStream.closeEntry();
				}
				zipStream.close();

			} catch (IOException ex) {

				logger.info("EDRMDownloadFilter Error " + ex.getMessage());

			}
			logger.info("Response Byte Stream length" + copy.length);

			logger.info("Response Byte Stream "
					+ new String(copy, response.getCharacterEncoding())); // Do

		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		logger.info("EDRMDownloadFilter intialized");

	}

}
