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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Script gerador de banco de dados de teste para questionário utilizado no IFC em 2016 
 * com entrevistados e respostas aleatórias.
 * 
 * Aproximadamente 35 minutos executando em minha máquina para criar o database.
 * 
 * @author Valter Estevam
 */
public class GeradorBaseDadosTesteIFC {

    public static void adicionarOpcoes(List<Opcao> opcoes, QuestaoMultiplaEscolha qme) throws PersistenceException {
        Dao<QuestaoMultiplaEscolha> questaoMuliplaEscolhaDAO = new GenericDAO<>(QuestaoMultiplaEscolha.class);
        Dao<Opcao> opcaoDAO = new GenericDAO<>(Opcao.class);

        for (Opcao o : opcoes) {
            opcaoDAO.salvar(o);
            qme.getOpcoes().add(o);
        }
        questaoMuliplaEscolhaDAO.alterar(qme);
    }

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

    public static void main(String[] args) throws PersistenceException, HashGenerationException, ParseException {

        GeraTabelas geraTabelas = new GeraTabelas();
        geraTabelas.gerarTabelas();
        
        /**
         * Baseado no questionário de 2016
         */
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
        SimpleDateFormat sdfDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
        Campus c1 = new Campus(0, "Campus Abelardo Luz", "Abelardo Luz");
        Campus c2 = new Campus(0, "Campus Araquari", "Araquari");
        Campus c3 = new Campus(0, "Campus Blumenau", "Blumenau");
        Campus c4 = new Campus(0, "Campus Brusque", "Brusque");
        Campus c5 = new Campus(0, "Campus Camboriú", "Camboriú");
        Campus c6 = new Campus(0, "Campus Concórdia", "Concórdia");
        Campus c7 = new Campus(0, "Campus Fraiburgo", "Fraiburgo");
        Campus c8 = new Campus(0, "Campus Ibirama", "Ibirama");
        Campus c9 = new Campus(0, "Campus Luzerna", "Luzerna");
        Campus c10 = new Campus(0, "Campus Rio do Sul", "Rio do Sul");
        Campus c11 = new Campus(0, "Campus Santa Rosa do Sul", "Santa Rosa do Sul");
        Campus c12 = new Campus(0, "Campus São Bento do Sul", "São Bento do Sul");
        Campus c13 = new Campus(0, "Campus São Francisco do Sul", "São Francisco do Sul");
        Campus c14 = new Campus(0, "Campus Sombrio", "Sombrio");
        Campus c15 = new Campus(0, "Campus Videira", "Videira");
        Campus c16 = new Campus(0, "Reitoria", "Reitoria");

        List<Campus> campi = new ArrayList<>();
        campi.add(c1);
        campi.add(c2);
        campi.add(c3);
        campi.add(c4);
        campi.add(c5);
        campi.add(c6);
        campi.add(c7);
        campi.add(c8);
        campi.add(c9);
        campi.add(c10);
        campi.add(c11);
        campi.add(c12);
        campi.add(c13);
        campi.add(c14);
        campi.add(c15);
        campi.add(c16);

        for (Campus cam : campi) {
            campusDAO.salvar(cam);
        }

        /**
         * Criar categorias
         */
        Categoria cat1 = new Categoria(0, "Docentes", "DOC");
        Categoria cat2 = new Categoria(0, "Técnicos Administrativos", "TAE");
        Categoria cat3 = new Categoria(0, "Discentes", "DIS");
        categoriaDAO.salvar(cat1);
        categoriaDAO.salvar(cat2);
        categoriaDAO.salvar(cat3);

        /**
         * Criar entrevistados
         */
        for (Campus cam : campi) {

            int qtdDOC = 30 + random.nextInt(70); // gera uma quantidade de professores aleatória entre 30 e 100 por campus
            int qtdTAE = 30 + random.nextInt(50); // gera uma quantidade de técnicos aleatória entre 30 e 80 por campus
            int qtdDIS = 300 + random.nextInt(900); // gera uma quantidade de alunos aleatória entre 300 e 1200 por campus

            if (cam.getNomeCompleto().equals("Reitoria")) {
                //criar apenas técnicos
                for (int i = 0; i <= qtdTAE; i++) {
                    entrevistadoDAO.salvar(new Entrevistado(0, "Técnico" + String.valueOf(i) + " - " + cam.getNomeCurto(),
                            new Date(), Digest.hashString(GeradorBaseDadosTeste.gerarCPF(), "SHA-256"),
                            "tecnico" + String.valueOf(i) + cam.getNomeCurto() + "@gmail.com",
                            Digest.hashString(GeradorBaseDadosTeste.gerarSenha(10), "SHA-256"),
                            cam, cat2, true));
                }
            } else {
                //criar professores
                for (int i = 0; i <= qtdDOC; i++) {
                    entrevistadoDAO.salvar(new Entrevistado(0, "Professor" + String.valueOf(i) + " - " + cam.getNomeCurto(),
                            new Date(), Digest.hashString(GeradorBaseDadosTeste.gerarCPF(), "SHA-256"),
                            "professor" + String.valueOf(i) + cam.getNomeCurto() + "@gmail.com",
                            Digest.hashString(GeradorBaseDadosTeste.gerarSenha(10), "SHA-256"),
                            cam, cat1, true));
                }
                //criar técnicos
                for (int i = 0; i <= qtdTAE; i++) {
                    entrevistadoDAO.salvar(new Entrevistado(0, "Técnico" + String.valueOf(i) + " - " + cam.getNomeCurto(),
                            new Date(), Digest.hashString(GeradorBaseDadosTeste.gerarCPF(), "SHA-256"),
                            "tecnico" + String.valueOf(i) + cam.getNomeCurto() + "@gmail.com",
                            Digest.hashString(GeradorBaseDadosTeste.gerarSenha(10), "SHA-256"),
                            cam, cat2, true));
                }
                //criar alunos
                for (int i = 0; i <= qtdDIS; i++) {
                    entrevistadoDAO.salvar(new Entrevistado(0, "Aluno" + String.valueOf(i) + " - " + cam.getNomeCurto(),
                            new Date(), Digest.hashString(GeradorBaseDadosTeste.gerarCPF(), "SHA-256"),
                            "aluno" + String.valueOf(i) + cam.getNomeCurto() + "@gmail.com",
                            Digest.hashString(GeradorBaseDadosTeste.gerarSenha(10), "SHA-256"),
                            cam, cat3, true));
                }
            }
        }

        /**
         * Criar questionários
         */
        Opcao op1t1 = new Opcao(0, "Não sei responder","Não sei responder");
        Opcao op2t1 = new Opcao(0, "Muito ruim","Muito ruim");
        Opcao op3t1 = new Opcao(0, "Ruim","Ruim");
        Opcao op4t1 = new Opcao(0, "Neutro","Neutro");
        Opcao op5t1 = new Opcao(0, "Bom","Bom");
        Opcao op6t1 = new Opcao(0, "Muito bom","Muito bom");
        List<Opcao> templateP1 = new ArrayList<>();
        templateP1.add(op1t1);
        templateP1.add(op2t1);
        templateP1.add(op3t1);
        templateP1.add(op4t1);
        templateP1.add(op5t1);
        templateP1.add(op6t1);

        Opcao op1t2 = new Opcao(0, "Não sei responder","Não sei responder");
        Opcao op2t2 = new Opcao(0, "Sim","Sim");
        Opcao op3t2 = new Opcao(0, "Não","Não");
        List<Opcao> templateP2 = new ArrayList<>();
        templateP2.add(op1t2);
        templateP2.add(op2t2);
        templateP2.add(op3t2);

        Questionario questionarioGeral = new Questionario(0,
                "Autoavaliação 2016 - Questionário Geral",
                "2016 - Questionário Geral",
                sdfDataHora.parse("09/08/2019 00:00"),
                sdfDataHora.parse("19/08/2019 23:59"),
                "", "");
        questionarioDAO.salvar(questionarioGeral);

        questionarioGeral.getCategorias().add(cat1);
        questionarioGeral.getCategorias().add(cat2);
        questionarioGeral.getCategorias().add(cat3);
        questionarioDAO.alterar(questionarioGeral);
                              
        Map<Integer, String[]> mapaQuestoes = new HashMap<>();

        String q1[] = {"Você tomou conhecimento dos resultados das últimas avaliações institucionais?", "Eixo 1 - Planejamento e Avaliação Institucional", "P2"};
        String q2[] = {"Você julga que as questões levantadas contribuem para o aprimoramento da instituição?","Eixo 1 - Planejamento e Avaliação Institucional","P2"};
        String q3[] = {"Você julga que as questões levantadas contribuem para o aprimoramento da instituição?","Eixo 1 - Planejamento e Avaliação Institucional","P1"};
        String q4[] = {"Nossa missão constante no PDI é: Proporcionar educação profissional, atuando em ensino, pesquisa e extensão, comprometidos com a formação cidadã, a inclusão social e o desenvolvimento regional. Você avalia que as ações da Administração estão alinhadas com esses propósitos?","Eixo 2 - Desenvolvimento Institucional","P2"};
        String q5[] = {"O seu campus promove a inclusão social de pessoas com necessidades especiais (materiais didáticos, acessibilidade, rampas, elevadores, banheiros, mesas adaptadas, piso tátil, etc.)?","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q6[] = {"O respeito pelas diferenças de gênero, étnicas, religiosas e políticas da comunidade acadêmica, no seu campus, é","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q7[] = {"Seu campus promove atividades artísticas e culturais?","Eixo 2 - Desenvolvimento Institucional","P2"};
        String q8[] = {"Seu conhecimento das documentações oficiais (PDI, Regimento Interno, Plano Orçamentário e outros)","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q9[] = {"A divulgação das atividades previstas no calendário acadêmico e atividades extras em seu campus é","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q10[] = {"As ações de pesquisa, considerando divulgação, incentivo e apoio são","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q11[] = {"As ações de extensão, considerando divulgação, incentivo e apoio são","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q12[] = {"O seu interesse em participar de projetos de pesquisa e/ou extensão é","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q13[] = {"Na sua percepção, as atividades de pesquisa e/ou extensão desenvolvidas pelo campus contribuem com os setores econômicos e sociais de sua região?","Eixo 2 - Desenvolvimento Institucional","P1"};
        String q14[] = {"Você possui conhecimento das oportunidades de programas de cooperação exVistentes?","Eixo 2 - Desenvolvimento Institucional","P2"};
        String q15[] = {"Você conhece o NGA – Núcleo de Gestão Ambiental – e suas ações no campus?","Eixo 2 - Desenvolvimento Institucional","P2"};
        String q16[] = {"Você tomou conhecimento dos resultados das últimas avaliações institucionais?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q17[] = {"A interação do IFC com a sociedade, em redes sociais, jornais, TV, Rádio e outros meios é","Eixo 3 - Políticas Acadêmicas","P1"};
        String q18[] = {"Como você avalia a disponibilização das informações no site da instituição?","Eixo 3 - Políticas Acadêmicas","P1"};
        String q19[] = {"A divulgação das atividades previstas no calendário acadêmico e das atividades extras em seu campus é","Eixo 3 - Políticas Acadêmicas","P1"};
        String q20[] = {"As ações de pesquisa, considerando divulgação, incentivo e apoio são","Eixo 3 - Políticas Acadêmicas","P1"};
        String q21[] = {"O seu interesse em participar de projetos de pesquisa e/ou extensão é","Eixo 3 - Políticas Acadêmicas","P1"};
        String q22[] = {"Na sua percepção, as atividades de pesquisa e/ou extensão desenvolvidas pelo campus contribuem com os setores econômicos e sociais de sua região?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q23[] = {"A possibilidade de participação em eventos (palestras, seminários, congressos, viagens de estudos) dentro e fora da instituição é:","Eixo 3 - Políticas Acadêmicas","P1"};
        String q24[] = {"O seu conhecimento referente ao NIT (Núcleo de Inovação Tecnológica) e suas ações no campus é","Eixo 3 - Políticas Acadêmicas","P1"};
        String q25[] = {"As ações e deliberações dos conselhos e colegiados (Consuper, Codir, NDE, ConCampus, Colegiado do Curso e outros) são socializadas para os alunos e servidores?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q26[] = {"No seu campus, as ações administrativas e pedagógicas são transparentes?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q27[] = {"Considera a quantidade e qualidade de materiais didáticos e recursos audiovisuais em sala de aula e laboratórios suficientes?","Eixo 3 - Políticas Acadêmicas","P1"};
        String q28[] = {"Você pensa em continuar sua formação na área de estudo em que você está hoje?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q29[] = {"A possibilidade de participação em eventos (palestras, seminários, congressos, viagens de estudos) dentro e fora da instituição é","Eixo 4 - Políticas de Gestão","P1"};
        String q30[] = {"O atendimento da secretaria acadêmica/coordenação de registros escolares, em relação às suas necessidades, é satisfatório?","Eixo 4 - Políticas de Gestão","P2"};
        String q31[] = {"Considera a quantidade e a qualidade de materiais didáticos, recursos audiovisuais, em sala de aula e laboratórios, suficientes?","Eixo 5 - Infraestrutura Física","P2"};
                            
        mapaQuestoes.put(1, q1);
        mapaQuestoes.put(2,q2);
        mapaQuestoes.put(3,q3);
        mapaQuestoes.put(4,q4);
        mapaQuestoes.put(5,q5);
        mapaQuestoes.put(6,q6);
        mapaQuestoes.put(7,q7);
        mapaQuestoes.put(8,q8);
        mapaQuestoes.put(9,q9);
        mapaQuestoes.put(10,q10);
        mapaQuestoes.put(11,q11);
        mapaQuestoes.put(12,q12);
        mapaQuestoes.put(13,q13);
        mapaQuestoes.put(14,q14);
        mapaQuestoes.put(15,q15);
        mapaQuestoes.put(16,q16);
        mapaQuestoes.put(17,q17);
        mapaQuestoes.put(18,q18);
        mapaQuestoes.put(19,q19);
        mapaQuestoes.put(20,q20);
        mapaQuestoes.put(21,q21);
        mapaQuestoes.put(22,q22);
        mapaQuestoes.put(23,q23);
        mapaQuestoes.put(24,q24);
        mapaQuestoes.put(25,q25);
        mapaQuestoes.put(26,q26);
        mapaQuestoes.put(27,q27);
        mapaQuestoes.put(28,q28);
        mapaQuestoes.put(29,q29);
        mapaQuestoes.put(30,q30);
        mapaQuestoes.put(31,q31);
        
        for (int i = 1; i <= 31; i++){
            
            String s[] = mapaQuestoes.get(i);
            QuestaoMultiplaEscolha qme = new QuestaoMultiplaEscolha(1, 1, new HashSet<Opcao>(), 0,s[0],"",s[1], true, i, questionarioGeral);
            questaoMuliplaEscolhaDAO.salvar(qme);
            if (s[2].equals("P1")){
                GeradorBaseDadosTesteIFC.adicionarOpcoes(templateP1, qme);
            }else{
                GeradorBaseDadosTesteIFC.adicionarOpcoes(templateP2, qme);
            }
            questionarioGeral.getQuestoes().add(qme);
            questionarioDAO.alterar(questionarioGeral);
        }   
        
        /**
         * Questionário específico
         */
        Questionario questionarioEspecifico = new Questionario(0,
                "Autoavaliação 2016 - Questionário Específico",
                "2016 - Questionário Específico",
                sdfDataHora.parse("09/08/2019 00:00"),
                sdfDataHora.parse("19/08/2019 23:59"),
                "", "");
        questionarioDAO.salvar(questionarioEspecifico);

        questionarioEspecifico.getCategorias().add(cat1);
        questionarioEspecifico.getCategorias().add(cat2);
        questionarioDAO.alterar(questionarioEspecifico);
        
        
        Map<Integer, String[]> mapaQuestoesEspecificas = new HashMap<>();

        String q1E[] = {"A instituição possui o Informativo IFC, enviado via e-mail para todos os servidores. Você lê o informativo?", "Eixo 3 - Políticas Acadêmicas", "P2"};
        String q2E[] = {"Você conhece o serviço de ouvidoria da instituição?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q3E[] = {"Sua avaliação sobre os benefícios oferecidos pelos programas de assistência ao estudante","Eixo 3 - Políticas Acadêmicas","P1"};
        String q4E[] = {"Você tem conhecimento da existência de políticas de acompanhamento de egressos?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q5E[] = {"Você tem conhecimento da existência de ações/mecanismos para identificação de fatores responsáveis pela evasão?","Eixo 3 - Políticas Acadêmicas","P2"};
        String q6E[] = {"O IFC propicia oportunidades de formação e capacitação aos servidores?","Eixo 4 - Políticas de Gestão","P2"};
        String q7E[] = {"A sua participação, na elaboração do planejamento anual do seu campus, é","Eixo 4 - Políticas de Gestão","P1"};
        String q8E[] = {"Existe coerência entre os cursos ofertados e os critérios de alocação de recursos destinados ao campus?","Eixo 4 - Políticas de Gestão","P2"};
        String q9E[] = {"Considera que a forma com que o processo de avaliação do estágio probatório é realizado é adequada?","Eixo 4 - Políticas de Gestão","P2"};
        String q10E[] = {"Você tem conhecimento da existência de ações/mecanismos para identificação de fatores responsáveis pela evasão?","Eixo 4 - Políticas de Gestão","P2"};
        
        mapaQuestoesEspecificas.put(1, q1E);
        mapaQuestoesEspecificas.put(2, q2E);
        mapaQuestoesEspecificas.put(3, q3E);
        mapaQuestoesEspecificas.put(4, q4E);
        mapaQuestoesEspecificas.put(5, q5E);
        mapaQuestoesEspecificas.put(6, q6E);
        mapaQuestoesEspecificas.put(7, q7E);
        mapaQuestoesEspecificas.put(8, q8E);
        mapaQuestoesEspecificas.put(9, q9E);
        mapaQuestoesEspecificas.put(10, q10E);
        
        for (int i = 1; i <= 10; i++){
            
            String s[] = mapaQuestoesEspecificas.get(i);
            QuestaoMultiplaEscolha qme = new QuestaoMultiplaEscolha(1, 1, new HashSet<Opcao>(), 0,s[0],"",s[1], true, i, questionarioGeral);
            questaoMuliplaEscolhaDAO.salvar(qme);
            if (s[2].equals("P1")){
                GeradorBaseDadosTesteIFC.adicionarOpcoes(templateP1, qme);
            }else{
                GeradorBaseDadosTesteIFC.adicionarOpcoes(templateP2, qme);
            }
            questionarioEspecifico.getQuestoes().add(qme);
            questionarioDAO.alterar(questionarioEspecifico);
        }  
        
        
        /**
         * Criar respostas
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
        System.exit(0);
    }

}