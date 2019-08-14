/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.scripts;

import br.edu.ifpr.irati.mb.util.Digest;
import br.edu.ifpr.irati.configuracao.modelo.AutenticacaoEmail;
import br.edu.ifpr.irati.configuracao.modelo.PropriedadeEmail;
import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.dao.QuestionarioDAO;
import br.edu.ifpr.irati.exception.HashGenerationException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Administrador;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.ConfiguracaoWeb;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.modelo.Opcao;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.QuestaoAberta;
import br.edu.ifpr.irati.modelo.QuestaoMultiplaEscolha;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.Respondente;
import br.edu.ifpr.irati.modelo.Resposta;
import br.edu.ifpr.irati.modelo.RespostaAberta;
import br.edu.ifpr.irati.modelo.RespostaMultiplaEscolha;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Script gerador de banco de dados de teste totalmente aleatório.
 * 
 * Aproximadamente 12 minutos executando em minha máquina para criar o database.
 * 
 * @author Valter Estevam
 */
public class GeradorBaseDadosTeste {

    public static String gerarSenha(int tamanho) {
        String[] carct = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String s = "";
        for (int x = 0; x < tamanho; x++) {
            int j = (int) (Math.random() * carct.length);
            s += carct[j];
        }
        return s;
    }
    
    
    public static String gerarCPF() {
        int digito1 = 0, digito2 = 0, resto = 0;
        String nDigResult;
        String numerosConcatenados;
        String numeroGerado;

        Random numeroAleatorio = new Random();

        //numeros gerados
        int n1 = numeroAleatorio.nextInt(10);
        int n2 = numeroAleatorio.nextInt(10);
        int n3 = numeroAleatorio.nextInt(10);
        int n4 = numeroAleatorio.nextInt(10);
        int n5 = numeroAleatorio.nextInt(10);
        int n6 = numeroAleatorio.nextInt(10);
        int n7 = numeroAleatorio.nextInt(10);
        int n8 = numeroAleatorio.nextInt(10);
        int n9 = numeroAleatorio.nextInt(10);

        int soma = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10;

        int valor = (soma / 11) * 11;

        digito1 = soma - valor;

        //Primeiro resto da divisão por 11.
        resto = (digito1 % 11);

        if (digito1 < 2) {
            digito1 = 0;
        } else {
            digito1 = 11 - resto;
        }

        int soma2 = digito1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11;

        int valor2 = (soma2 / 11) * 11;

        digito2 = soma2 - valor2;

        //Primeiro resto da divisão por 11.
        resto = (digito2 % 11);

        if (digito2 < 2) {
            digito2 = 0;
        } else {
            digito2 = 11 - resto;
        }

        //Conctenando os numeros
        numerosConcatenados = String.valueOf(n1) + String.valueOf(n2) + String.valueOf(n3) + "." + String.valueOf(n4)
                + String.valueOf(n5) + String.valueOf(n6) + "." + String.valueOf(n7) + String.valueOf(n8)
                + String.valueOf(n9) + "-";

        //Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

        numeroGerado = numerosConcatenados + nDigResult;

        return numeroGerado;
    }

    /**
     * Criar uma base de dados para simulação de relatórios
     *
     * @param args
     * @throws PersistenceException
     * @throws HashGenerationException
     */
    public static void main(String[] args) throws PersistenceException, HashGenerationException {

        GeraTabelas geraTabelas = new GeraTabelas();
        geraTabelas.gerarTabelas();
        
                        
        Dao<PropriedadeEmail> propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
        Dao<AutenticacaoEmail> autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
        Dao<ConfiguracaoWeb> configuracaoWebDAO = new GenericDAO<>(ConfiguracaoWeb.class);
        Dao<Administrador> administradorDAO = new GenericDAO<>(Administrador.class);
        Dao<Campus> campusDAO = new GenericDAO<>(Campus.class);
        Dao<Categoria> categoriaDAO = new GenericDAO<>(Categoria.class);
        Dao<Entrevistado> entrevistadoDAO = new GenericDAO<>(Entrevistado.class);
        Dao<Questionario> questionarioDAO = new GenericDAO<>(Questionario.class);
        Dao<QuestaoAberta> questaoAbertaDAO = new GenericDAO<>(QuestaoAberta.class);
        Dao<QuestaoMultiplaEscolha> questaoMuliplaEscolhaDAO = new GenericDAO<>(QuestaoMultiplaEscolha.class);
        Dao<Opcao> opcaoDAO = new GenericDAO<>(Opcao.class);
        Dao<RespostaAberta> respostaAbertaDAO = new GenericDAO<>(RespostaAberta.class);
        Dao<RespostaMultiplaEscolha> respostaMultiplaEscolhaDAO = new GenericDAO<>(RespostaMultiplaEscolha.class);
        Dao<Respondente> respondenteDAO = new GenericDAO<>(Respondente.class);

        Random random = new Random();        

        /**
         * Criar lista de propriedades de e-mail
         */
        List<PropriedadeEmail> propriedades = new ArrayList<>();
        propriedades.add(new PropriedadeEmail(0, "mail.transport.protocol", "smtp"));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.starttls.enable", "true"));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.host", "mail.valterestevam.com.br"));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.auth", "true"));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.user", "saavidemo@valterestevam.com.br"));
        propriedades.add(new PropriedadeEmail(0, "mail.debug", "true"));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.port", "587"));
        propriedades.add(new PropriedadeEmail(0, "proxySet", ""));
        propriedades.add(new PropriedadeEmail(0, "socksProxyHost", ""));
        propriedades.add(new PropriedadeEmail(0, "socksProxyPort", ""));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.socketFactory.port", ""));
        propriedades.add(new PropriedadeEmail(0, "mail.smtp.socketFactory.fallback", ""));
        for (PropriedadeEmail p : propriedades) {
            propriedadeEmailDAO.salvar(p);
        }
        AutenticacaoEmail autenticacaoEmail = new AutenticacaoEmail(0, "saavidemo@valterestevam.com.br", "saaviifpr2019");
        autenticacaoEmailDAO.salvar(autenticacaoEmail);

        /**
         * Criar usuário administrador padrão
         */
        Administrador adm = new Administrador(0, "Administrador Geral", "saavidemo@valterestevam.com.br", Digest.hashString("admin", "SHA-256"));
        administradorDAO.salvar(adm);

        /**
         * Criar configuração padrão de dados a exibir na interface.
         */
        ConfiguracaoWeb configuracao = new ConfiguracaoWeb(0, "Nome da Instituição", "Endereço da Instituição", "Telefone da instituição", "E-mail para contato", "http://localhost:8080");
        configuracaoWebDAO.salvar(configuracao);

        /**
         * Criar campus
         */
        int qtdCampus = 25;
        for (int i = 1; i <= qtdCampus; i++) {
            campusDAO.salvar(new Campus(0, "Campus " + String.valueOf(i), String.valueOf(i)));
        }

        /**
         * Criar categorias
         */
        int qtdCategoria = 10;
        for (int i = 1; i <= qtdCategoria; i++) {
            categoriaDAO.salvar(new Categoria(0, "Categoria " + String.valueOf(i), String.valueOf(i)));
        }
        
        /**
         * Gerar entrevistados
         */
        int qtdEntrevistados = 5000;
        for (int i = 1; i <= qtdEntrevistados; i++){                 
            entrevistadoDAO.salvar(new Entrevistado(0,"Entrevistado "+String.valueOf(i),new Date(),GeradorBaseDadosTeste.gerarCPF(),"entrevistado"+String.valueOf(i)+"@gmail.com", Digest.hashString(GeradorBaseDadosTeste.gerarSenha(10),"SHA-256"), campusDAO.buscarPorId(random.nextInt(qtdCampus-1)+1), categoriaDAO.buscarPorId(random.nextInt(qtdCategoria-1)+1), true));
        }
        
        /**
         * Gerar questionários
         */
        int qtdQuestionarios = 2;
        
        int qtdQuestoesAbertas = 5;
        int qtdQuestoesMultiplaEscolha = 15;
        int qtdOpcoesPorQuestao = 5;
        int categoriasPorQuestionario = 7;
        
        for (int i = 1; i <= qtdQuestionarios; i++){
            
            Date dataHoraInicio = new Date();
            dataHoraInicio.setDate(dataHoraInicio.getDate()-2);
            Date dataHoraFim = new Date();
            dataHoraFim.setDate(dataHoraFim.getDate()+2);
            
            Questionario questionario = new Questionario(0, "Questionário exemplo "+String.valueOf(i),"Exemplo "+String.valueOf(i),dataHoraInicio,dataHoraFim,"Descrição do questionário "+String.valueOf(i),"");
            
            /*
                Sortear e adicionar categorias
            */
            List<Categoria> categorias = categoriaDAO.buscarTodos(Categoria.class);
            while(questionario.getCategorias().size() < categoriasPorQuestionario){
                int pos = random.nextInt(categorias.size()-1);
                questionario.getCategorias().add(categorias.get(pos));
            }
            
            questionarioDAO.salvar(questionario);
            
            /*
            Criar questões / adicionar ao questionário / atualizar banco de dados
            */
            int qtdQ = 1;
            for (int j = 1; j < qtdQuestoesMultiplaEscolha; j++){                
                QuestaoMultiplaEscolha qme = new QuestaoMultiplaEscolha(1, 2,new HashSet<Opcao>(),0,"Texto da questão "+String.valueOf(qtdQ),"Dica da Questão "+String.valueOf(qtdQ),"Rótulo da questão "+String.valueOf(qtdQ),true, qtdQ, questionario);                
                questaoMuliplaEscolhaDAO.salvar(qme);
                
                for (int z = 1; z <= qtdOpcoesPorQuestao; z++){
                    Opcao o = new Opcao(0,"Opção "+String.valueOf(z),"Opção "+String.valueOf(z));
                    opcaoDAO.salvar(o);
                    qme.getOpcoes().add(o);
                }                
                questaoMuliplaEscolhaDAO.alterar(qme);

                questionario.getQuestoes().add(qme);
                questionarioDAO.alterar(questionario);
                
                qtdQ++;                
            }
            for (int j = 1; j < qtdQuestoesAbertas; j++){                
                QuestaoAberta qa = new QuestaoAberta(100,0,"Texto da questão "+String.valueOf(qtdQ),"Dica da questão "+String.valueOf(qtdQ),"Rótulo da questão "+String.valueOf(qtdQ), true, qtdQ,questionario);
                questaoAbertaDAO.salvar(qa); 
                
                questionario.getQuestoes().add(qa);
                questionarioDAO.alterar(questionario);
            }            
            
        }
        
        /**
         * Criar respostas para os questionários
         */
        QuestionarioDAO questionarioDAO1 = new QuestionarioDAO();
        
        List<Entrevistado> entrevistados = entrevistadoDAO.buscarTodos(Entrevistado.class);
        
        for (Entrevistado e: entrevistados){
            
            List<Questionario> questionariosAbertos = questionarioDAO1.buscarQuestionariosAtivos(e);
            
            for (Questionario questionario: questionariosAbertos){
                
                List<Resposta> respostas = new ArrayList<>();
                
                for (Questao q: questionario.getQuestoes()){
                    
                    if (q instanceof QuestaoAberta){
                        QuestaoAberta qa = (QuestaoAberta) q;
                        RespostaAberta ra = new RespostaAberta("texto da questão aberta "+String.valueOf(qa.getIdQuestao()),0,new Date(),qa, e);
                        respostas.add(ra);                        
                    
                    }else if (q instanceof QuestaoMultiplaEscolha){
                        QuestaoMultiplaEscolha qme = (QuestaoMultiplaEscolha) q;
                                                       
                        List<Opcao> listaOpcoes = new ArrayList<>(qme.getOpcoes());
                        Collections.sort(listaOpcoes);
                        Set<Opcao> opcoes = new TreeSet<>();
                        while (opcoes.size() < qme.getQuantidadeMaxima()){
                            int n = random.nextInt(qme.getOpcoes().size());
                            opcoes.add(listaOpcoes.get(n));
                        }                        
                        RespostaMultiplaEscolha rme = new RespostaMultiplaEscolha(opcoes,0,new Date(), qme, e);
                        respostas.add(rme);
                        
                    }                                        
                }
                
                for (Resposta resposta: respostas){                    
                    if (resposta instanceof RespostaAberta){
                        respostaAbertaDAO.salvar((RespostaAberta) resposta);
                    }else{
                        respostaMultiplaEscolhaDAO.salvar((RespostaMultiplaEscolha) resposta);
                    }                    
                }
                
                Respondente respondente = new Respondente(0, e, questionario, new Date());
                respondenteDAO.salvar(respondente);
                                
            }
                        
        }                                
        System.out.println("\n\n Concluído \n\n");
        System.exit(0);
    }
}
