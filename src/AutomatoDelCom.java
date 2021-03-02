import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AutomatoDelCom {
    public static void main(String [] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        File lista_arquivos[];
        File diretorio = new File("./input");
        lista_arquivos = diretorio.listFiles();
        //System.out.println(diretorio.listFiles()[0].toPath());

        for(File arquivo:lista_arquivos){
            BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
            int estado = 0;
            String linha = leitor.readLine();
            char[] c =  linha.toCharArray();
            int atual = 0;
            //System.out.println(c.length);
            while(linha != null){
                //System.out.println(linha);
                if(atual < c.length) {
                    System.out.println(c[atual]+ "  " + atual);
                }
                if(linha.length() > 0) {
                    switch (estado) {
                        case 0:
                            System.out.println("0");
                            if (c[atual] == '/') {
                                estado = 1;
                                //Requisitar o proximo caractere
                                atual++;
                                if(atual >= (c.length - 1)){
                                    relatar_erro();
                                    estado = 0;
                                    atual++;
                                }
                            }else if(c[atual] == ' ') {
                                estado = 0;
                                atual++;
                            }else {
                                //Ir para um estado que reconhece outro token ou dar erro
                                System.out.println("Outro estado");
                            }
                            break;
                        case 1:
                            System.out.println("1");
                            if (c[atual] == '*') {
                                estado = 2;
                                //Requisitar o proximo caractere
                                atual++;
                            } else if (c[atual] == '/') {
                                estado = 5;
                                //Requisitar o proximo caractere
                                atual++;
                            } else {
                                relatar_erro();
                                estado = 0;
                                atual++;
                            }
                            break;
                        case 2:
                            System.out.println("2");
                            if (atual >= (c.length - 1)) {
                                //Se o buffer de entrada acabar, recarregar
                                linha = leitor.readLine();
                                c = linha.toCharArray();
                                atual = 0;
                            } else if (c[atual] == '*') {
                                estado = 3;
                                //Requisitar o proximo caractere
                                atual++;
                            }else{
                                estado = 2;
                                //Requisitar o proximo caractere
                                atual++;
                            }
                            break;
                        case 3:
                            System.out.println("3");
                            if (c[atual] == '/') {
                                estado = 4;
                            } else {
                                estado = 2;
                                atual++;
                            }
                            break;
                        case 4:
                            System.out.println("4");
                            // Estado final
                            atual++;
                            estado = 0;
                            break;
                        case 5:
                            System.out.println("5");
                            if (atual >= (c.length - 1)) {
                                estado = 4;
                            } else {
                                estado = 5;
                                //Requisitar o proximo caractere
                                atual++;
                            }
                            //System.out.println(estado);
                            break;
                    /*case 6:
                        // Estado final
                        estado = 0;
                        break;*/
                    }
                }
                if(atual >= c.length && (estado != 2 && estado != 4)){
                    System.out.println("foi");
                    linha = leitor.readLine();
                    if(linha != null) {
                        c = linha.toCharArray();
                    }
                    atual = 0;
                }
            }

        }

    }

    private static void relatar_erro() {
        //Escreve o erro na saída;
        System.out.println("Deu merda");
    }

    private static void proxima_linha(){
        //Pega a proxima linha do arquivo;

    }

}
