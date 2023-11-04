package ContactManagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;      
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//image packages
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.geometry.Pos;

class Contact extends HBox{
    private Label index;
    private TextField contactName;
    private TextField contactNumber;
    private TextField contactEmail;
    private Button imageButton;

    private Button selectButton;
    private boolean selectContact;

    private ImageView imageView;
    

    Contact(){
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        selectContact = false;

        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(index); // add index label to task

        imageButton = new Button("Upload Image");
        imageButton.setShape(new Circle(30));
        imageButton.setMaxSize(40,40);

        imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageButton.setGraphic(imageView);

        contactName = new TextField(); // create task name text field
        contactName.setPrefSize(120, 20); // set size of text field
        contactName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        contactName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field

        contactNumber = new TextField(); // create task name text field
        contactNumber.setPrefSize(120, 20); // set size of text field
        contactNumber.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        contactNumber.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field

        contactEmail = new TextField(); // create task name text field
        contactEmail.setPrefSize(150, 20); // set size of text field
        contactEmail.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        contactEmail.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        
        contactName.setEditable(false);
        contactNumber.setEditable(false);
        contactEmail.setEditable(false);

        selectButton = new Button("Edit"); // creates a button for marking the task as done
        selectButton.setPrefSize(50, 20);
        selectButton.setPrefHeight(Double.MAX_VALUE);
        selectButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // sets style of button

        this.getChildren().addAll(imageButton, contactName, contactNumber, contactEmail, selectButton);
    }

    public void setContactIndex(int num){
        this.index.setText(num + "");
        this.contactName.setPromptText("Name" + num);
        this.contactNumber.setPromptText("Number " + num);
        this.contactEmail.setPromptText("Email " + num);
    }

    public TextField getContactName() {
        return this.contactName;
    }

    public TextField getContactNumber() {
        return this.contactNumber;
    }

    public TextField getContactEmail() {
        return this.contactEmail;
    }

    public Button getImageButton(){
        return this.imageButton;
    }

    public ImageView getImageView() {
    return this.imageView;
    }

    public Button getSelectButton(){
        return this.selectButton;
    }
    public boolean isSelected(){
        return this.selectContact;
    }

    public void toggleSelected() {
        if (this.selectContact == true) {
            this.selectContact = false;
            contactName.setEditable(false);
            contactNumber.setEditable(false);
            contactEmail.setEditable(false);
            this.setStyle("-fx-border-color: #000000; -fx-border-width: 0; -fx-font-weight: bold;"); // remove border of task
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // change color of task to gray
            }
        }
        else {
            selectContact = true;
            contactName.setEditable(true);
            contactNumber.setEditable(true);
            contactEmail.setEditable(true);
            this.setStyle("-fx-border-color: #000000; -fx-border-width: 0; -fx-font-weight: bold;"); // remove border of task
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #BCE29E; -fx-border-width: 0;"); // change color of task to green
            }
        }
    }
}

class ContactList extends VBox{
    private ImageView imageView = new ImageView();
    private FileChooser fileChooser = new FileChooser();

    ContactList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateContactIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                ((Contact) this.getChildren().get(i)).setContactIndex(index);
                index++;
            }
        }
    }

    public void deleteContacts(){
        this.getChildren().removeIf(contact -> contact instanceof Contact && ((Contact) contact).isSelected());
        this.updateContactIndices();
    }

    /*
     * Save tasks to a file called "contacts.csv"
     */
    public void exportContacts() {
        File file = new File("contacts.csv");
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("Name;Number;Email\n");

            for(int i = 0; i < this.getChildren().size(); i++){
                if(this.getChildren().get(i) instanceof Contact){
                    Contact contact = (Contact) this.getChildren().get(i);
                    String name = contact.getContactName().getText();
                    String number = contact.getContactNumber().getText();
                    String email = contact.getContactEmail().getText();

                    try{
                        bufferedWriter.write(name + ";" + number + ";" + email + "\n");
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }  
            }
            try {
                bufferedWriter.close();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Sort the tasks lexicographically
     */
    public void sortContacts() {
        // Stores the items into an ArrayList to sort
        ArrayList<String> contacts = new ArrayList<String>();
        for (int i = 0; i < this.getChildren().size(); i++) {
            contacts.add(((Contact) this.getChildren().get(i)).getContactName().getText());
        }

        Collections.sort(contacts);
 
        // Traverses the current list and updates based on alphabetical order
        for (int j = 0; j < this.getChildren().size(); j++) {
            Contact task = (Contact) this.getChildren().get(j);
            task.getContactName().setText(contacts.get(j));
        }
    }
}

class Footer extends HBox{
    private Button addButton;
    private Button sortButton;
    private Button exportButton;
    private Button deleteButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Contact"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        sortButton = new Button("Sort Contacts");
        sortButton.setStyle(defaultButtonStyle);

        deleteButton = new Button("Delete Selected Contact");
        deleteButton.setStyle(defaultButtonStyle);

        exportButton = new Button("Export Contacts");
        exportButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton, sortButton, deleteButton, exportButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    public Button getSortButton() {
        return sortButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}

class Header extends HBox{
    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Contact Management App"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{
    private Header header;
    private Footer footer;
    private ContactList contactList;
    private Contact contact;

    private Button addButton;
    private Button exportButton;
    private Button sortButton;
    private Button deleteButton;

    private Button imageButton;
    private ImageView imageView;
    AppFrame()
    {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        contactList = new ContactList();
        
        // Initialise the Footer Object
        footer = new Footer();

        contact = new Contact();
        // TODO: Add a Scroller to the Task List
        // hint 1: ScrollPane() is the Pane Layout used to add a scroller - it will take the tasklist as a parameter
        // hint 2: setFitToWidth, and setFitToHeight attributes are used for setting width and height
        // hint 3: The center of the AppFrame layout should be the scroller window instead  of tasklist
        ScrollPane scroller = new ScrollPane(contactList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);



        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroller);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        addButton = footer.getAddButton();
        exportButton = footer.getExportButton();
        sortButton = footer.getSortButton();
        deleteButton = footer.getDeleteButton();
        imageButton = contact.getImageButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {
        // Add button functionality
        addButton.setOnAction(e -> {
            Contact contact = new Contact();
            contactList.getChildren().add(contact);
            Button imageButton = contact.getImageButton();
            ImageView imageView = contact.getImageView(); // Add a getter for imageView in Contact class
        
            // Add FileChooser here for the image button
            imageButton.setOnAction(ie -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image); // Set image to ImageView
            }
        });

        Button selectButton = contact.getSelectButton();
        selectButton.setOnAction(e1 -> {
            contact.toggleSelected();
        });

        contactList.updateContactIndices();
        });

        exportButton.setOnAction(e -> {
            contactList.exportContacts();
        });

        sortButton.setOnAction(e -> {
            contactList.sortContacts();
        });

        deleteButton.setOnAction(e -> {
            contactList.deleteContacts();
        });


    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();
        // Set the title of the app
        primaryStage.setTitle("Contact Management App");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


