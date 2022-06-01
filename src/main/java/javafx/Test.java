package javafx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lexer.Lexer;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;


/**
 * @author 阮启腾
 * @description
 * @date 2022/3/3,23:05
 */
public class Test extends Application{
    public String openFileName = "";

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane pane = (AnchorPane) fxmlLoader.load(new FileInputStream(new File("src/main/resources/fxml/Test.fxml")));

//        MenuBar menuBar = new MenuBar();
//        pane.setTop(menuBar);
//        Menu fileMenu = new Menu("文件");
//        MenuItem newFileMenu = new MenuItem("新建");
//        MenuItem saveFileMenu = new MenuItem("保存");
//        MenuItem saveAsFileMenu = new MenuItem("另存为");
//        MenuItem openFileMenu = new MenuItem("打开文件");
//        fileMenu.getItems().addAll(newFileMenu, saveAsFileMenu, saveFileMenu,openFileMenu);
//
//        //新建文件快捷键
//        newFileMenu.setAccelerator(KeyCombination.valueOf("ctrl+N"));
//        //保存文件快捷键
//        saveFileMenu.setAccelerator(KeyCombination.valueOf("ctrl+S"));
//
//        Menu menu1 = new Menu("语法分析");
//        Menu menu2 = new Menu("词法分析");
//        Menu menu3 = new Menu("编辑");
//        Menu menu4 = new Menu("编译");
//        Menu menu5 = new Menu("帮助");
//
//        MenuItem analyse = new MenuItem("分析");
//        menu2.getItems().add(analyse);
//
//        TextArea textArea1 = new TextArea();
//
////        pane.setLeft(textArea1);
//        pane.setLeft(anchorPane);
//        TextArea textArea2 = new TextArea();
//        pane.setBottom(textArea2);
//        TableView<Object> tableView = new TableView<>();
//        TableColumn<Object, Object> firstColumn = new TableColumn<>("单词");
//        TableColumn<Object, Object> secondColumn = new TableColumn<>("单词类型");
//        TableColumn<Object, Object> thirdColumn = new TableColumn<>("token值");
//        tableView.getColumns().addAll(firstColumn, secondColumn, thirdColumn);
//
//
//        TextArea textArea3 = new TextArea();
////
//
//        TextArea textArea4 = new TextArea();
//        pane.setRight(textArea4);
//        pane.setCenter(tableView);
//        menuBar.getMenus().addAll(fileMenu, menu1, menu2, menu3,menu4,menu5);
//
//        ListView<Integer> view = new ListView<>();
//        view.setPrefWidth(10);
//
//        anchorPane.getChildren().addAll(view, textArea1);
//
//        //点击打开文件子菜单弹出窗口
//        openFileMenu.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.getExtensionFilters().addAll(new
//                        FileChooser.ExtensionFilter("所有类型", "*.*"));
//                File file = fileChooser.showOpenDialog(new Stage());
//                if (file != null) {
//                    try {
//
//                        String s = lexer.readText(file.getAbsolutePath());
//                        textArea1.appendText(s);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(file.getAbsolutePath());
//                    openFileName = file.getAbsolutePath();    //保存刚才打开的文件的全路径
//                }
//            }
//        });
//
//        //点击词法分析菜单项
//        analyse.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                textArea4.clear();
//                try {
//                    lexer.text = "";    //先清空文件里的内容
//                    lexer.readText(openFileName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("点击词法分析菜单项");
//                lexer.scannerAll(textArea4);
//            }
//        });
//
//        //保存文件
//        saveFileMenu.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                File file = new File(openFileName);
//                String text = textArea1.getText();
//                try {
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
//                    outputStreamWriter.write(text);
//                    outputStreamWriter.close();
//                    fileOutputStream.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        Scene scene = new Scene(pane);
        stage.setTitle("编译器");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

}
