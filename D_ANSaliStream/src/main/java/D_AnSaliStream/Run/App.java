package D_AnSaliStream.Run;


import javax.swing.UIManager;

import GUIs.InputBroker;
//import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel;
//import de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel;
//import de.javasoft.plaf.synthetica.SyntheticaSkyMetallicLookAndFeel;

/**
 * Hello world!
 *
 */
public class App
{
	static{
		String[] path = System.getProperty("user.dir").split("D_ANSaliStream");
		String str = path[0] + "/db/opencv_java310.dll";
		System.load(str);
	}
	
    public static void main( String[] args )
    {
    	try {
           /* for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }*/
    		UIManager.setLookAndFeel(new SyntheticaBlueIceLookAndFeel());
        /*} catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/}
    		catch(Exception e){
    			e.printStackTrace();
    		}
        //</editor-fold>
    	/*String[] path = System.getProperty("user.dir").split("D_ANSaliStream");
    	System.out.println(path[0]);*/
    	/*Properties p = System.getProperties();
    	   p.list(System.out);*/
    	new InputBroker().setVisible(true);

    }
}
