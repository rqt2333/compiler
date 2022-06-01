package ToTargetCode;

import javafx.scene.control.TextArea;
import lexer.ReadAndWriteFile;
import semantic.LiveStatus;
import semantic.Quaternary;
import semantic.SymbolTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/23,21:25
 */
public class ToNasmCode {
    private ArrayList<String> asmCode = new ArrayList<>();//存储汇编代码
    private ArrayList<String> preAsmCode = new ArrayList<>();//存储汇编代码
    private int asmCount = 0;//跳转计数器
    private int[] asmJump;//记录每个四元式对应的第一条指令位置
    private int msgCount = 0;

    public void cToAsm(SymbolTable table, LiveStatus LiveStatus,Object... args) {
        asmJump = new int[LiveStatus.getQt().size()];
//        preAsmCode.add("global _start");  //定义入口函数
//        preAsmCode.add("extern _printf");  //导入printf输出
//        preAsmCode.add("section .data");  //数据段
//        for (int i = 0; i < table.Const.size(); i++) {
//            if (table.Const.get(i).type.equals("int") || table.Const.get(i).type.equals("char") || table.Const.get(i).type.equals("float")) {
//                preAsmCode.add("    "+table.Const.get(i).name + "   equ   " + table.Const.get(i).value);
//            }
//        }

//        preAsmCode.add("    fmt: db '%ld',0xa,0");

//        preAsmCode.add("section .bss");  //数据段
//        for (int i = 0; i < table.SynTable.size(); i++) {
//            if (table.SynTable.get(i).type.equals("int") || table.SynTable.get(i).type.equals("char") || table.SynTable.get(i).type.equals("float")) {
//                preAsmCode.add(table.SynTable.get(i).name + ": resb 4");
//            }
//        }


//        preAsmCode.add("section .text");;
//        preAsmCode.add("_start:");
//        preAsmCode.add("    push rbp");

        for (int i = 0; i < LiveStatus.getQt().size(); i++) {
            Quaternary temp = LiveStatus.getQt().get(i);
            //如果四元式的首符号是+
            if (temp.getOperator().getWord().equals("+")) {
                //如果第二个操作符是常数
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                //如果第三个操作符是常数
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    add rax, rbx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("-")) {//如果四元式的首符号是-
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    sub rax, rbx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("*")) {//如果四元式的首符号是*
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    mul rbx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("/")) {//如果四元式的首符号是/
                asmCode.add("    mov rdx, 0");
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    div rbx");
                asmCode.add("    mov rdx, rcx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 6;     //这个语句里面添加了六条指令
            } else if (LiveStatus.getQt().get(i).getOperator().getWord().equals("=")) {//赋值语句
                String name = LiveStatus.getQt().get(i).getArg1().getName();
                //name为int_const，如果包含const，说明是常数
                if (LiveStatus.getQt().get(i).getArg1().getName().contains("const")){
                    //rax跟ax是同一寄存器，rax支持64位，ax支持16位
                    asmCode.add("    mov rax, " + LiveStatus.getQt().get(i).getArg1());
                    //将寄存器的值传给内存中的距离基地址a偏移量的内存单元
                    asmCode.add("    mov [rel " + LiveStatus.getQt().get(i).getResult() + "], rax");
                }else {
                    asmCode.add("    mov rax, [rel " + LiveStatus.getQt().get(i).getArg1() + "]");
                    asmCode.add("    mov [rel " + LiveStatus.getQt().get(i).getResult() + "], rax");
                }
                asmJump[i] = asmCount;
                asmCount += 2;

            } else if (temp.getOperator().getWord().equals("==")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("je " + LiveStatus.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals(">")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                //大于则跳转
                asmCode.add("jg " + LiveStatus.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("<")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jl " + LiveStatus.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals(">=")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jge " + LiveStatus.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("<=")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jle " + LiveStatus.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            }
            //跳转指令
            else if (temp.getOperator().getWord().equals("j")) {
                asmCode.add("jmp " + temp.getResult());
                asmJump[i] = asmCount;
                asmCount += 1;
            } else if (temp.getOperator().getWord().equals("print")) {
                if (temp.getResult().getName().contains("const")){
                    asmCode.add("   mov rax, 0x2000004");
                    asmCode.add("   mov rdi, 1");
                    asmCode.add("   mov rsi, msg" + msgCount);
                    asmCode.add("   mov rdx, msg_len" + msgCount);
                    asmCode.add("   syscall");
                    addMsg("msg" + msgCount + ": db " + temp.getResult() + ",10\nmsg_len" + msgCount + ": equ $ - msg" + msgCount);
                }else {
                    asmCode.add("    mov rdi, fmt");
                    asmCode.add("    mov rax, [rel " + temp.getResult() + "]");
                    asmCode.add("    mov rsi, rax");
                    asmCode.add("    mov rax, 0");
                    asmCode.add("    call _printf");
                }
                asmJump[i] = asmCount;
                asmCount += 5;
            }
        }



        asmJump[LiveStatus.getQt().size() - 1] = asmCode.size();

//        asmCode.add("    pop rbp");
////        asmCode.add("    mov rax, 60");
//        asmCode.add("    syscall");    //
//        asmCode.add("    ret");

        Map<String, String> labelMap = new LinkedHashMap<>();
        AtomicInteger labelnum = new AtomicInteger();
        asmCode.forEach(s -> {
            //如果以j开头
            if (s.startsWith("j")){
                String[] getJumpNum = s.split(" ");
                String toWhere = getJumpNum[1];
                if (labelMap.get(toWhere) == null){
                    labelMap.put(toWhere, "TURN" + labelnum.getAndIncrement());
                }
            }
        });

        for (int j = 0; j < asmCode.size(); j++) {
            if (asmCode.get(j).startsWith("j")){
                String[] getJumpNum = asmCode.get(j).split(" ");
                String toWhere = getJumpNum[1];
                if (labelMap.get(toWhere) == null){
                    labelMap.put(toWhere, "TURN" + labelnum.getAndIncrement());
                }
            }
        }

        for (int j = 0; j < asmCode.size(); j++) {
            if (asmCode.get(j).startsWith("j")){
                String[] getJumpNum = asmCode.get(j).split(" ");
                String toWhere = getJumpNum[1];
                //动态替换元素
                asmCode.set(j, getJumpNum[0] + " " + labelMap.get(toWhere));
            }
        }


        labelMap.forEach((k, v)->{
            String temp = asmCode.get(asmJump[Integer.parseInt(k)]);
            asmCode.set(asmJump[Integer.parseInt(k)], v + ":\n" + temp);
        });

//        asmCode.forEach(System.out::println);
        if (args.length > 0) {
            TextArea textArea = (TextArea)args[0];
            textArea.appendText("汇编代码如下：\n");
            asmCode.forEach(k->{
                textArea.appendText(k + "\n");
            });
        }
    }

    public void addMsg(String msg){
        for (int i = 0; i < preAsmCode.size(); i++){
            if (preAsmCode.get(i).equals("section .data")){
                preAsmCode.add(i + 1, msg);
                msgCount++;
                return;
            }
        }
    }

    public ArrayList<String> getAsmCode() {
        return asmCode;
    }

    public ArrayList<String> getPreAsmCode() {
        return preAsmCode;
    }

//    public List<String> getResults() throws IOException {
//        List<String> results = new ArrayList<>(preAsmCode);
//        results.addAll(asmCode);
//        StringBuilder content = new StringBuilder();
//        results.forEach(s -> {
//            content.append(s).append("\n");
//        });
//        ReadAndWriteFile.write("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/pers/lomesome/compliation/utils/toasm/test.asm", content.toString());
//        return results;
//    }
}
