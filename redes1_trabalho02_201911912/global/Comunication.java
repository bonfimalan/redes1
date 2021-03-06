/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:57
 * Last update: 16/08/2021 19:54
 * Name: Comunication.java
 * Function: Simulates the comunication mean
 *******************************************************************/
package global;

import controllers.MainController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import receiver.ReceiverPhisicalLayer;

public class Comunication { 
  private static int canvasXPosition;

  public static void comunicate(int[] transmitterBits, MainController controller){
    //related to the GUI--------------------------------------------------------------------
    TextArea codificationTextArea = controller.getReceiverCodificationTextArea();
    Canvas canvas = controller.getCanvas();
    ScrollBar scroll = controller.getScroll();
    GraphicsContext graContext = canvas.getGraphicsContext2D();

    graContext.setFill(Color.WHITE);
    canvas.setLayoutX(0);//reset the position of the canvas
    //set the width of the canvas, using the size of the array * the size of each line
    canvas.setWidth(transmitterBits.length * 22);

    //set the max value of the scroll, it's based in the size of the canvas - the size of the
    //scroll wich is the size of the with of the window
    scroll.setValue(0);//reset the value to the scroll
    scroll.setDisable(true);//disable the scroll
    scroll.setMax(canvas.getWidth() - scroll.getWidth());

    //paints the all canvas with white
    graContext.fillRect(0, 0, canvas.getWidth(), 50);

    //changes the color that are used to paint the canvas to black
    graContext.setFill(Color.BLACK);
    canvasXPosition = 0;

    //changing to the bits tab
    controller.getReceiverTabpane().getSelectionModel().select(
      controller.getReceiverTabpane().getTabs().get(0)
    );

    //creates a listener that force the scroll to be set at the bottom
    ChangeListener<Number> listener = new ChangeListener<Number>(){
      @Override
      public void changed(ObservableValue<? extends Number> v, Number oldValue, Number newValue){
        codificationTextArea.setScrollTop(9999);//make the scroll go down if the texts exceeds the size of the TextArea
      }
    };
    codificationTextArea.scrollTopProperty().addListener(listener);

    //--------------------------------------------------------------------------------------
    
    int[] receiverBits = new int[transmitterBits.length];

    new Thread( () -> {
      try{
        for(int i=0; i<transmitterBits.length; i++){
          receiverBits[i] = transmitterBits[i];
            
          //related to the GUI-------------------------------------------------------------------------------------
          final int POSITION = i;//the runLater don't accept the local variable i
          //adding a bit to the textArea
          Platform.runLater( () -> codificationTextArea.setText(codificationTextArea.getText() + receiverBits[POSITION]));
          //drawing in the canvas-----------
          if(receiverBits[i] == 1)
            Platform.runLater( () -> graContext.fillRect(canvasXPosition, 10, 20, 3));//draws a horizontal line int the canvas
          else
            Platform.runLater( () -> graContext.fillRect(canvasXPosition, 32, 20, 3));//draws a horizontal line int the canvas
          if(i != 0 && receiverBits[i] != receiverBits[i-1])
            Platform.runLater( () -> graContext.fillRect(canvasXPosition, 13, 3, 16));//draws a vertical line int the canvas
          //--------------------------------

          //if the canvas pass the size of the window it will be moved
          if(canvasXPosition > 1024){
            canvas.setLayoutX(canvas.getLayoutX() - 21);
          }

          canvasXPosition += 21;//calculates the new position to draw on the canvas, wich is the size of the line + 1
          //-------------------------------------------------------------------------------------------------------

          Thread.sleep(Variables.speed);//hmmmm soninho
        }//end for

        scroll.setValue(scroll.getMax());//set the scroll to the max value
        scroll.setDisable(false);//enable the scroll
        codificationTextArea.scrollTopProperty().removeListener(listener);

        //send the bits to the receiver phisical layer
        ReceiverPhisicalLayer.receive(receiverBits, controller);
      } catch(InterruptedException e) { }
    }).start();//end thread
  }//end comunicate


  /**********************************************
   * The code below is not used, it was my firt
   * attempt, since I miss understood the problem
   * and how it showed be done
   **********************************************/
  /* 
  public static void comunicate(int[] transmitterBits, int codificationType, MainController controller){
    int[] receiverBits = new int[transmitterBits.length];

    int temp;
    Canvas canvas = controller.getCanvas();
    ScrollBar scroll = controller.getScroll();
    GraphicsContext graContext = canvas.getGraphicsContext2D();
    graContext.setFill(Color.WHITE);
    canvas.setLayoutX(0);
    scroll.setValue(0);
    canvas.setWidth(transmitterBits.length * 7 * 16);
    scroll.setMax(canvas.getWidth() - scroll.getWidth());
    graContext.fillRect(0, 0, canvas.getWidth(), 50);
    graContext.setFill(Color.BLACK);
    canvasXPosition = 0;


    for(int i = 0; i<transmitterBits.length; i++){
      receiverBits[i] = transmitterBits[i];
      temp = receiverBits[i];
      
      //changes that are made to the interface
      while(temp != 0){
        if(temp % 10 == 1)
          graContext.fillRect(canvasXPosition, 5, 15, 3);
        else
          graContext.fillRect(canvasXPosition, 32, 15, 3);
        canvasXPosition +=16;
        temp /= 10;
      }
    }
    ReceiverPhisicalLayer.receives(receiverBits, null, codificationType, controller.getReceiver());
  }

  public static void comunicate(String[] transmitterBits, int codificationType, MainController controller) {
    String[] receiverBits = new String[transmitterBits.length];
    for(int i = 0; i<transmitterBits.length; i++){
      receiverBits[i] = transmitterBits[i];
    }

    ReceiverPhisicalLayer.receives(null, receiverBits, codificationType, controller.getReceiver());
  }
  */
}
