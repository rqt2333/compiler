package controller;

import MinDfa.MinDfaGo;
import NfaToDfa.NfaToDfaGo;
import RegToNfa.RegToNfaGo;
import ToTargetCode.ToNasmCode;
import javafx.Test;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lexer.*;
import semantic.Analysis;
import syntax.SyntaxAnalysis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/3/18,12:58
 */
public class MyController {

    public String openFileName = "";
    public Lexer lexer = new Lexer();
    public TextArea editArea;
    public TextArea resultArea;
    public TextArea resultArea1;
    public TextArea resultArea2;

    public MyController() throws IOException {

    }

    @FXML
    public void initialize() throws IOException {
        System.out.println("初始化");
    }

    public void openFile(ActionEvent actionEvent) {
        editArea.clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new
                FileChooser.ExtensionFilter("所有类型", "*.*"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String s = lexer.readText(file.getAbsolutePath());
                editArea.appendText(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(file.getAbsolutePath());
            openFileName = file.getAbsolutePath();    //保存刚才打开的文件的全路径
        }
    }

    //点击词法分析器
    public void analyse(ActionEvent actionEvent) {
        resultArea2.clear();
        resultArea1.clear();
        resultArea.clear();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(openFileName);
        lexicalAnalyzer.runAnalyzer();
        List<Word> words = lexicalAnalyzer.getWords();
        List<Word> errorMsgList = lexicalAnalyzer.getErrorMsgList();
        for (Word word : words) {
            resultArea.appendText(word.getWord() + "                 " + word.getTypenum()+"\n");
        }
        resultArea1.appendText("点击词法分析菜单项\n");
        resultArea1.appendText("正在分析……\n");
        resultArea1.appendText("分析完成……\n");

        if (errorMsgList.size() == 0) {
            resultArea2.appendText("没有出现错误");
        } else {
            for (Word word : errorMsgList) {
                resultArea2.appendText(word + " " + word.getType() + " " + word.getRow() + " " + word.getCol()+"\n");
            }
        }

//        try {
//            lexer.text = "";    //先清空文件里的内容
//            lexer.row=1;
//            lexer.readText(openFileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ObservableList<Data> list = FXCollections.observableArrayList();
//        lexer.scannerAll(list);
//        tableView.setItems(list);
//        wordColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Data, String> param) {
//                return new SimpleStringProperty(param.getValue().getWord());
//            }
//        });
//        wordTypeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Data, String> param) {
//                return new SimpleStringProperty(param.getValue().getWordType());
//            }
//        });
//        tokenColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Data, String> param) {
//                return new SimpleStringProperty(param.getValue().getToken());
//            }
//        });
//        System.out.println("点击词法分析菜单项");

    }

    //保存当前文件
    public void saveFile(ActionEvent actionEvent) {
        File file = new File(openFileName);
        String text = editArea.getText();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(text);
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //点击NFA_DFA_MFA
    public void NFA_DFA_MFA(ActionEvent actionEvent) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        // 设置弹出框大小
        Scene scene = new Scene(anchorPane,850, 600);

        TextField textField = new TextField();
        textField.setLayoutX(50);
        textField.setLayoutY(50);
        anchorPane.getChildren().add(textField);
//        Button button1 = new Button("验证正规式");
        Button reg_nfa = new Button("Reg---NFA");
        Button nfa_dfa = new Button("NFA---DFA");
        Button dfa_mfa = new Button("DFA---MFA");
//        button1.setLayoutX(50);
        reg_nfa.setLayoutX(50);
        nfa_dfa.setLayoutX(50);
        dfa_mfa.setLayoutX(50);
//        button1.setLayoutY(100);
        reg_nfa.setLayoutY(150);
        nfa_dfa.setLayoutY(200);
        dfa_mfa.setLayoutY(250);
        anchorPane.getChildren().addAll( reg_nfa, nfa_dfa, dfa_mfa);

        //reg----> nfa
        reg_nfa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = textField.getText();
                RegToNfaGo regToNfaGo = new RegToNfaGo();
                regToNfaGo.go(text);
            }
        });

        //nfa----> dfa
        nfa_dfa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = textField.getText();
                NfaToDfaGo nfaToDfaGo = new NfaToDfaGo();
                nfaToDfaGo.go(text);
            }
        });

        //dfa----> mfa
        dfa_mfa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = textField.getText();
                MinDfaGo minDfaGo = new MinDfaGo();
                minDfaGo.go(text);
            }
        });

        stage.setScene(scene);
        // 设置弹出框标题
        stage.setTitle("NFA_DFA_MFA");
        // 设置弹出框大小是否可变
        stage.setResizable(false);
        stage.setOnCloseRequest(null);
        stage.show();
    }

//    //点击FIRST_FOLLOW选项
//    public void FIRST_FOLLOW(ActionEvent actionEvent) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        AnchorPane pane = (AnchorPane) fxmlLoader.load(new FileInputStream(new File("src/main/resources/fxml/first_follow.fxml")));
//        Scene scene = new Scene(pane);
//        Stage stage = new Stage();
//        stage.setTitle("FIRST_FOLLOW");
//        stage.setScene(scene);
//        stage.setResizable(true);
//        stage.show();
//    }

    //点击预测分析方法
    public void predict(ActionEvent actionEvent) {
        resultArea2.clear();
        resultArea1.clear();
        resultArea.clear();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(openFileName);
        lexicalAnalyzer.runAnalyzer();
        List<Word> list = new LinkedList<>();
        List<Word> words = lexicalAnalyzer.getWords();

        for (Word word : words) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            System.out.println(word);
            list.add(word);
            resultArea.appendText(word.getWord() + "                 " + word.getTypenum()+"\n");
        }
        System.out.println("================================");
        list.add(new Word("#", "end", -1,-1));
//
        list.get(list.size() - 1).setName("#");
        System.out.println("进行初始化……");
        resultArea1.appendText("进行初始化……\n");
        SyntaxAnalysis.init();
        resultArea1.appendText("初始化完成\n");
        resultArea1.appendText("==============================\n");
        resultArea1.appendText("开始语法分析……\n");
        resultArea1.appendText("==============================\n");

        System.out.println("语法分析开始\n");
        SyntaxAnalysis.analysis(list,resultArea2,resultArea1);
        resultArea1.appendText("==============================\n");
        resultArea1.appendText("结束语法分析……\n");

    }


    //生成语法树
    public void generateGrammarTree(ActionEvent actionEvent) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(openFileName);
        lexicalAnalyzer.runAnalyzer();
        List<Word> list = new LinkedList<>();
        List<Word> words = lexicalAnalyzer.getWords();

        for (Word word : words) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            list.add(word);
        }
        list.add(new Word("#", "end", -1,-1));
//
        list.get(list.size() - 1).setName("#");
        SyntaxAnalysis.init();
        SyntaxAnalysis.analysis(list);
        SyntaxAnalysis.drawTree();
    }

    //点击生成中间代码
    public void generateMiddleCode(ActionEvent actionEvent) {
        resultArea2.clear();
        resultArea1.clear();
        resultArea.clear();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(openFileName);
        lexicalAnalyzer.runAnalyzer();
        List<Word> list = new LinkedList<>();
        List<Word> words = lexicalAnalyzer.getWords();

        for (Word word : words) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            System.out.println(word);
            list.add(word);
        }
        System.out.println("================================");
        list.add(new Word("#", "end", -1,-1));
//
        list.get(list.size() - 1).setName("#");

        SyntaxAnalysis.init();
        Analysis.init(list);
        Analysis.analyse(list,resultArea,resultArea1);

    }

    //生成汇编代码
    public void generateAsmCode(ActionEvent actionEvent) {
        resultArea2.clear();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(openFileName);
        lexicalAnalyzer.runAnalyzer();
        List<Word> list = new LinkedList<>();
        List<Word> words = lexicalAnalyzer.getWords();

        for (Word word : words) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            System.out.println(word);
            list.add(word);
        }
        System.out.println("================================");
        list.add(new Word("#", "end", -1,-1));
//
        list.get(list.size() - 1).setName("#");

        SyntaxAnalysis.init();
        Analysis.init(list);
        Analysis.analyse(list);
        ToNasmCode toNasmCode = new ToNasmCode();
        toNasmCode.cToAsm(FinalAttribute.getSymbolTable("main"), FinalAttribute.getSymbolTable("main").getLiveStatus(),resultArea2);

    }
}

