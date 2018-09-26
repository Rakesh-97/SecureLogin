import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import javax.imageio.ImageIO;
 
@WebServlet("/ForRegestrationServlet")
public class ForRegestrationServlet extends HttpServlet {
	
	public String outputvalue;
	
    private static final long serialVersionUID = 1L;
    private static BufferedImage image=null,new_image=null;
    private static int pixels[][];
    private static char Data[];
    private static int Width,Height;
    private static int Binary[];
    private static int Length=0; 
 
    public ForRegestrationServlet() {
        super();
 
    }
 
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        response.getOutputStream().println("Hurray !! This Servlet Works");
 
    }
 
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        try {
        	
        	request.getParameter("user");
        	String Pw = request.getParameter("pass");
        	String PIname = request.getParameter("PassImage");

        	String Binary_Pw = text_to_binary(Pw);
        	
        	Stegnography("/home/choudhary/eclipse-workspace/Demo/src/"+PIname,
        			"/home/choudhary/eclipse-workspace/Demo/src/PIStagoImage.png", Binary_Pw);
        	
        	File piStagnoImage = new File("/home/choudhary/eclipse-workspace/Demo/src/PIStagoImage.png"); 
            BufferedImage buffImg = ImageIO.read(piStagnoImage);
        	String Ii = hash_of_image(buffImg);
        	
        	String Pw_plus_Ii = Pw+Ii;
        	String Pw_plus_Ii_hash = get_SHA_512_SecureText(Pw_plus_Ii,"india");
        	
        	response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.append(Pw_plus_Ii_hash);  
            writer.flush();
            writer.close();
        	
        } catch (IOException e) {
 
 
            try{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
            }
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}   
 }
 
    public static String text_to_binary(String texttobinary){
        byte[] bytes = texttobinary.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
           int val = b;
           for (int i = 0; i < 8; i++)
           {
              binary.append((val & 128) == 0 ? 0 : 1);
              val <<= 1;
           }
           
        }
          return binary.toString();
      }
    
    public static String XOR_of_two_Binary_String(String str1, String str2){
        
        int maxlength=0,diff=0;
        String addstr;
        int strlen1 = str1.length();
        int strlen2 = str2.length();
        if(strlen2<=strlen1){
            addstr="";
            maxlength = strlen1;
            diff = strlen1 - strlen2;
            for(int i=0;i<diff;i++)
                addstr+="0";
            System.out.println(addstr);
            str2=addstr+str2;
        }
        else{
            addstr="";
            maxlength = strlen2;
            diff = strlen2 - strlen1;
            for(int i=0;i<diff;i++){
                addstr+="0";
            str1=addstr+str1;
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < maxlength; i++)
            sb.append((str1.charAt(i) ^ str2.charAt(i)));

        String result = sb.toString();
        
        return result;
    }
    
    public static String text_to_hash(String passwordToHash, String   salt){

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    public static void Stegnography(String file1, String file2, String name){
        
        
        File fileName = new File(file1);
        File fileName1 = new File(file2);
            
            try{
                    new_image= ImageIO.read(fileName);
                    image=new BufferedImage(new_image.getWidth(), new_image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);                       
                     Graphics2D graphics   = image.createGraphics();
                     graphics.drawRenderedImage(new_image, null);
                     graphics.dispose();
            }
            catch (IOException e){
                    e.printStackTrace();
            }
            
            pixels= new int[image.getWidth()][image.getHeight()];
            Binary= new int[8];                
            Width=image.getWidth();
            Height=image.getHeight();               
            for(int i=0;i<Width;i++){
                    for(int j=0;j<Height;j++){
                            pixels[i][j]=image.getRGB(i, j);
                    }
            }
            Length=name.length();
            Data=name.toCharArray();
            WriteLSB();
            try{
                    ImageIO.write(image, "png", fileName1);
            }
            catch (IOException e) {
                    e.printStackTrace();
            }
            
    }
    
    public static void WriteLSB()
    {               
            int x=0,p=0,i,j=0;
            Binary(Data[x]);
            x++;
            for(i=0;(i<Width && x<Data.length);i++){
                    for(j=0;( j<Height && x<Data.length);j++){
                            int change=0;                                   
                            for(int k=0;k<4;k++){
                                    if (k==0){
                                            change=1;
                                    }
                                    else
                                            if(k==1){
                                                    change=256;
                                            }
                                            else
                                                    if(k==2){
                                                            change=65536;
                                                    }
                                                    else
                                                        if(k==3){
                                                                    change = 16777216;
                                                            }
                            
                                    if(Binary[p]==0) {
                                            pixels[i][j]= pixels[i][j] & ~change; // ~1 ki all bits 1 hoti ha except LSB
                                    }
                                    else if(Binary[p]==1){
                                           pixels[i][j]= pixels[i][j] | change; // only LSB ko 1 krna ha
                                    }
                                    p++;
                                    
                                    if(p==8){
                                            p=0;
                                            Binary(Data[x]);
                                            x++;
                                    }
                            }

                    }
            }

            for(i=0;i<Width;i++){
                    for(int h=0;h<Height;h++){
                            image.setRGB(i, h,pixels[i][h]);
                    }
            }
            
    }
    
    public static void Binary(int temp){
            Binary=null;
            Binary= new int[8];
            
            for(int i=7;i>=0;i--){
                    Binary[i]=temp%2; 
                    temp/=2;
            }
           
    }      

    public static String hash_of_image(BufferedImage buffImg) throws NoSuchAlgorithmException, IOException, Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(buffImg, "png", outputStream);
        byte[] data = outputStream.toByteArray();

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(data);
        byte[] hash = md.digest();
        return(returnHex(hash));
        
        
    }                                       
    static String returnHex(byte[] inBytes) throws Exception {
        String hexString = "";
            for (int i=0; i < inBytes.length; i++) {
                hexString +=
                Integer.toString( ( inBytes[i] & 0xff ) + 0x100, 16).substring( 1 );
            }                                   
        return hexString;
  }
    
    public String get_SHA_512_SecureText(String passwordToHash, String   salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
