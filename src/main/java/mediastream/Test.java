package mediastream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.file.Files;

import com.google.common.io.ByteStreams;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

	public static void main(String[] args) {
		
		try {
			//ServerSocket server = new ServerSocket(1234);
			//URL url = new URL("http", "localhost", 1234, "");
			//URLConnection conection = url.openConnection();
			//conection.setDoOutput(true);
			
			HttpServer httpsrv = HttpServer.create(new InetSocketAddress(1234), 0);
			httpsrv.createContext("/", new HttpHandler() {
				
				public void handle(HttpExchange arg0) throws IOException {
					int tam = 1024*1024;
					int rango=0;
					
					System.out.println();
					
					OutputStream out = null;
					FileInputStream in = null;
					
					try {
					
						out = arg0.getResponseBody();
						//RandomAccessFile file = new RandomAccessFile("/home/alberto/video.avi", "r");
						
						File file = new File("/home/alberto/video.avi");
						in = new FileInputStream(file);
						
						//InputStream in = Channels.newInputStream(file.getChannel());
						
						String range = arg0.getRequestHeaders().getFirst("Range");
						if (range != null) {
							
							System.out.println(range);
							
							range = range.substring(range.indexOf("=") + 1, range.indexOf("-"));
							rango = (new Integer(range)).intValue();
							
							System.out.println("rango: " + rango);
							
							arg0.getResponseHeaders().set("Accept-Ranges", "bytes");
							arg0.getResponseHeaders().set("Content-Type", Files.probeContentType(new File("/home/alberto/video.avi").toPath()));
							arg0.getResponseHeaders().set("Content-Length", String.valueOf(file.length() - rango + 1));
							System.out.println("Content-Length: "+ String.valueOf(file.length() - rango + 1));
							arg0.getResponseHeaders().set("Content-Range", "bytes " + rango + "-" + file.length() + "/" + file.length() );
							System.out.println("Content-Range: " + "bytes " + rango + "-" + file.length() + "/" + file.length() );
							
							long skiped = in.skip(rango);
							System.out.println("skiped: " + skiped);
						
							arg0.sendResponseHeaders(206, 0);//rango);//mifile.length());
							
						} else {
							
							
							arg0.getResponseHeaders().set("Accept-Ranges", "bytes");
							arg0.getResponseHeaders().set("Content-Type", Files.probeContentType(new File("/home/alberto/video.avi").toPath()));
							arg0.getResponseHeaders().set("Content-Length", String.valueOf(file.length()));
							arg0.getResponseHeaders().set("Content-Range", "bytes " + rango + "-" + file.length() + "/" + file.length() );
							arg0.sendResponseHeaders(200, 0);//rango);//mifile.length());
							
						}
						
						//ByteStreams.copy(in, out);
						
						//File file = new File("/home/alberto/video.avi");
						//FileInputStream in = new FileInputStream(file);
						
						//byte[] buffer = new byte[tam];
						
						if (!arg0.getRequestMethod().equals("HEAD")) {
						
							byte[] buffer = new byte[tam];
							while (in.read(buffer) > 0 && out != null ) {
								out.write(buffer);
								out.flush();
							}
							
						}
						
						//if (in.read(buffer) > 0) {
						//	out.write(buffer);
						//}
						
					} finally {
					
						//mifichero.close();
						in.close();
						//file.close();
						out.close();
					
					}
					
					System.out.println("Petición terminada.");
				}
			});
			
			httpsrv.setExecutor(null);
			httpsrv.start();
			
			System.out.println("Servidor arrancado.");
			
			//File mifile = new File("/home/alberto/video.avi");
			//FileInputStream mifichero = new FileInputStream(mifile);

			//byte[] buffer = new byte[1024];
			
			//Socket client = server.accept();
			//DataOutputStream out = new DataOutputStream(client.getOutputStream());
			//OutputStream out = client.getOutputStream();
			
			//DataOutputStream out = new DataOutputStream(conection.getOutputStream());
			
			//mifichero.skip(1024*1024);
			
			//while (mifichero.read(buffer, 0, 1024) > 0) {
				
			//	out.write(buffer, 0, 1024);
				
			//}
			
			//mifichero.close();
			//client.close();
			//server.close();
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
