/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:53
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package transmitter;

import controllers.MainController;
import javafx.scene.control.TextArea;
import receiver.ReceiverApplication;

public class TransmitterApplication {
  private TextArea textArea;
  private ReceiverApplication receiver;

  public TransmitterApplication(TextArea textArea, ReceiverApplication receiver){
    this.textArea = textArea;
    this.receiver = receiver;
  }

  //sends the mensage to the phisical layer
  public void sendToPhisicalLayer(MainController controller){
    char[] message = textArea.getText().toCharArray();
    int[] asciiMessage = new int[message.length];
    //transforms from char in int, with the ascii code
    for(int i=0; i<message.length; i++)
      asciiMessage[i] = message[i];
    
    //sends the message to the phisical layer
    TransmitterPhisicalLayer.receiveFromApplicationLayer(asciiMessage, 0, controller);
  }
}
