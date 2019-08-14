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
package br.edu.ifpr.irati.dao;

import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.Opcao;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.QuestaoAberta;
import br.edu.ifpr.irati.modelo.QuestaoMultiplaEscolha;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.Respondente;
import br.edu.ifpr.irati.modelo.relatorio.Registro;
import br.edu.ifpr.irati.modelo.relatorio.RegistroAberta;
import br.edu.ifpr.irati.modelo.relatorio.RegistroMultiplaEscolha;
import br.edu.ifpr.irati.modelo.relatorio.Tabela;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * Classe para acesso aos dados do relatório.
 * 
 * @author Valter Estevam
 */
public class RelatorioDAO {

    /**
     * Retorna uma listagem de tabelas criadas dados os parâmetros fornecidos.
     * 
     * @param questionario questionário para o qual o relatório está sendo criado.
     * @param questao questão para a qual se deseja obter as tabelas.
     * @param campus listagem de campus selecionados pelo usuário.
     * @param categorias listagem de categorias selecionadas pelo usuário.
     * @return listagem de tabelas usadas na interface gráfica para apresentar os dados.
     */
    public List<Tabela> criarTabelasPorQuestao(Questionario questionario, Questao questao, List<Campus> campus, List<Categoria> categorias) {
        List<Questao> questoes = new ArrayList<>(questionario.getQuestoes());
        Collections.sort(questoes);
        List<Tabela> tabelas = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        if (questao instanceof QuestaoMultiplaEscolha) {
            QuestaoMultiplaEscolha qme = (QuestaoMultiplaEscolha) questao;
            List<Opcao> opcoes = new ArrayList<>(qme.getOpcoes());
            Collections.sort(opcoes);
            for (Campus cam : campus) {
                if (cam.getIdCampus() == -1) {
                    continue;
                }
                Tabela tabela = new Tabela();
                tabela.setTitulo(cam.getNomeCurto());
                for (Opcao o : opcoes) {
                    tabela.getColunas().add(o.getTextoCurto());
                }
                List<Registro> registros = new ArrayList<>();
                for (Categoria cat : categorias) {
                    RegistroMultiplaEscolha registro = new RegistroMultiplaEscolha();
                    registro.setTitulo(cat.getRotuloCurto());
                    Map<String, Integer> valoresAbsolutos = new HashMap<>();
                    for (Opcao o : opcoes) {
                        String sql = "select count(*) from resposta r, respostamultiplaescolha rme, respondente rnd, entrevistado e, respostamultiplaescolha_opcao rmo where r.idResposta = rme.idResposta and rnd.validado = 'Y' and rnd.entrevistado_idEntrevistado = r.entrevistado_idEntrevistado and rmo.respostamultiplaescolha_idResposta = r.idResposta and rnd.questionario_idQuestionario = " + questionario.getIdQuestionario() + " and e.idEntrevistado = rnd.entrevistado_idEntrevistado and r.entrevistado_idEntrevistado = e.idEntrevistado and e.campus_idCampus = " + cam.getIdCampus() + " and e.categoria_idCategoria = " + cat.getIdCategoria() + " and rmo.opcoes_idOpcao = " + o.getIdOpcao();
                        Query querySQL = session.createSQLQuery(sql);
                        List resp = querySQL.list();
                        int valor = ((BigInteger) resp.get(0)).intValue();
                        valoresAbsolutos.put(o.getTextoCurto(), valor);
                    }
                    registro.setValoresAbsolutos(valoresAbsolutos);
                    registros.add(registro);
                }
                tabela.setRegistros(registros);
                tabela.setMultiplaEscolha(true);
                tabelas.add(tabela);
            }
        } else {
            QuestaoAberta qa = (QuestaoAberta) questao;
            for (Campus cam : campus) {
                if (cam.getIdCampus() == -1) {
                    continue;
                }
                for (Categoria cat : categorias) {
                    Tabela tabela = new Tabela();
                    tabela.setTitulo(cam.getNomeCurto());
                    tabela.getColunas().add(cat.getRotuloCurto());
                    List<Registro> registros = new ArrayList<>();

                    String sql = "select ra.nomecompleto from resposta r, respostaaberta ra, respondente rnd, entrevistado e where r.idResposta = ra.idResposta and rnd.validado = 'Y' and rnd.entrevistado_idEntrevistado = r.entrevistado_idEntrevistado and r.questao_idQuestao = " + questao.getIdQuestao() + " and rnd.questionario_idQuestionario = " + questionario.getIdQuestionario() + " and e.idEntrevistado = rnd.entrevistado_idEntrevistado and r.entrevistado_idEntrevistado = e.idEntrevistado and e.campus_idCampus = " + cam.getIdCampus() + " and e.categoria_idCategoria = " + cat.getIdCategoria();
                    Query querySQL = session.createSQLQuery(sql);
                    List<String> resp = querySQL.list();
                    for (String s : resp) {
                        RegistroAberta registroAberta = new RegistroAberta(s, cat.getRotuloCompleto());
                        registros.add(registroAberta);
                    }
                    tabela.setRegistros(registros);
                    tabela.setMultiplaEscolha(false);
                    tabelas.add(tabela);
                }
            }
        }
        session.getTransaction().commit();
        session.close();

        /**
         * Calcular os valores totais
         */
        for (Campus cam : campus) {
            if (cam.getIdCampus() != -1) {
                continue;
            }
            if (questao instanceof QuestaoMultiplaEscolha) {
                QuestaoMultiplaEscolha qme = (QuestaoMultiplaEscolha) questao;
                List<Opcao> opcoes = new ArrayList<>(qme.getOpcoes());
                Collections.sort(opcoes);
                Tabela tabelaTotal = new Tabela();
                tabelaTotal.setTitulo("Total");
                tabelaTotal.setMultiplaEscolha(true);
                for (Opcao o : opcoes) {
                    tabelaTotal.getColunas().add(o.getTextoCurto());
                }
                //inicializar registros
                Tabela tbEx = tabelas.get(0);
                for (Registro rme : tbEx.getRegistros()) {
                    RegistroMultiplaEscolha regEx = (RegistroMultiplaEscolha) rme;
                    RegistroMultiplaEscolha novo = new RegistroMultiplaEscolha();
                    novo.setTitulo(regEx.getTitulo());
                    Map<String, Integer> valoresNovo = new HashMap<>();
                    for (Opcao o : opcoes) {
                        valoresNovo.put(o.getTextoCurto(), 0);
                    }
                    novo.setValoresAbsolutos(valoresNovo);
                    tabelaTotal.getRegistros().add(novo);
                }
                //somar os valores dos registros de todas as tabelas para chegar ao total
                for (Tabela t : tabelas) {

                    for (int i = 0; i < t.getRegistros().size(); i++) {
                        RegistroMultiplaEscolha rmeTb = (RegistroMultiplaEscolha) t.getRegistros().get(i);
                        RegistroMultiplaEscolha rmeNovo = (RegistroMultiplaEscolha) tabelaTotal.getRegistros().get(i);
                        for (Opcao o : opcoes) {
                            int valor = rmeNovo.getValoresAbsolutos().get(o.getTextoCurto()) + rmeTb.getValoresAbsolutos().get(o.getTextoCurto());
                            rmeNovo.getValoresAbsolutos().put(o.getTextoCurto(), valor);
                        }
                        rmeNovo.setValoresAbsolutos(rmeNovo.getValoresAbsolutos());
                    }
                }
                tabelas.add(tabelaTotal);
            } else {
                List<Tabela> novasTabelas = new ArrayList<>();

                for (Categoria cat : categorias) {
                    Tabela tabelaTotal = new Tabela();
                    tabelaTotal.setTitulo("Total");
                    tabelaTotal.setMultiplaEscolha(false);

                    for (Tabela t : tabelas) {
                        if (t.getColunas().get(0).equals(cat.getRotuloCurto())) {
                            for (Registro r : t.getRegistros()) {
                                RegistroAberta raT = (RegistroAberta) r;
                                RegistroAberta raNovo = new RegistroAberta(raT.getResposta(), raT.getTitulo());
                                tabelaTotal.getRegistros().add(raNovo);
                            }
                        }
                    }
                    List<String> colunas = new ArrayList<>();
                    colunas.add(cat.getRotuloCurto());
                    tabelaTotal.setColunas(colunas);
                    if (!tabelaTotal.getRegistros().isEmpty()) {
                        novasTabelas.add(tabelaTotal);
                    }

                }
                tabelas.addAll(novasTabelas);
            }
        }
        return tabelas;
    }

    /**
     * 
     * Retorna uma tabela apresentando o total de entrevistados por campus e categoria com quantitativos 
     * tanto em valores absolutos quanto em valores percentuais.
     * 
     * @param questionario questionário para o qual o relatório está sendo elaborado.
     * @param categorias relação de categorias selecionadas pelo usuário.
     * @param campi relação de campus selecionados pelo usuário.
     * @return 
     */
    public Tabela criarTabelaResumo(Questionario questionario, List<Categoria> categorias, List<Campus> campi) {
        /**
         * Gerar tabela de resumo dos respondentes por campus e categoria
         */
        RespondenteDAO respondenteDAO = new RespondenteDAO();
        List<Respondente> respondentes = new ArrayList<>();
        try {
            respondentes = respondenteDAO.buscarPorQuestionario(questionario);
        } catch (PersistenceException ex) {
        }

        List<String> categoriasColunas = new ArrayList<>();
        for (Categoria cat : categorias) {
            categoriasColunas.add(cat.getRotuloCurto());
        }

        List<Registro> registrosResumo = new ArrayList<>();
        for (Campus cam : campi) {

            RegistroMultiplaEscolha registro = new RegistroMultiplaEscolha();
            registro.setTitulo(cam.getNomeCurto());
            Map<String, Integer> valores = new HashMap<>();
            //inicializar os valores com zero
            for (String s : categoriasColunas) {
                //contar o número de entrevistados pelo campus e pela categoria
                valores.put(s, 0);
            }

            for (String s : categoriasColunas) {
                for (Respondente r : respondentes) {
                    if (cam.getIdCampus() != -1) {
                        if (r.getEntrevistado().getCampus().getIdCampus() == cam.getIdCampus() && r.getEntrevistado().getCategoria().getRotuloCurto().equals(s)) {
                            int atual = valores.get(s);
                            atual++;
                            valores.put(s, atual);
                        }
                    } else {
                        if (r.getEntrevistado().getCategoria().getRotuloCurto().equals(s)) {
                            int atual = valores.get(s);
                            atual++;
                            valores.put(s, atual);
                        }
                    }

                }
            }

            registro.setValoresAbsolutos(valores);
            registrosResumo.add(registro);
        }
        return new Tabela("Resumo dos entrevistados", registrosResumo, categoriasColunas);
    }

    /**
     * Altera o conteúdo do arquivo localizado no path dado por parâmetro incluindo os dados 
     * conforme os parâmetros informados.
     * 
     * @param questionario questionário para o qual o relatório está sendo emitido.
     * @param campi relação de campus selecionados pelo usuário.
     * @param categorias relação de categorias selecionadas pelo usuário.
     * @param path caminho do arquivo .csv onde os dados devem ser escritos.
     */
    public void exportarDadosEmArquivo(Questionario questionario, List<Campus> campi, List<Categoria> categorias, String path) {

        FileWriter arq = null;
        try {
            List<Questao> questoes = new ArrayList<>(questionario.getQuestoes());
            Collections.sort(questoes);
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction(); 
            arq = new FileWriter(path);
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.print("Campus;Categoria;Rótulo;Questão;Resposta\n");
            for (Campus campus : campi) {
                if (campus.getIdCampus() < 1){
                    continue;
                }                
                for (Categoria categoria : categorias) {
                    /**
                     * Carregar respostas de múltipla escolha
                     */
                    String sqlM = "select c.nomecompleto as campus, cat.rotulocompleto as categoria, q.rotulo as rotulo, q.texto as questao, o.textocompleto as resposta from resposta r, respostamultiplaescolha rme, respondente rnd, entrevistado e, respostamultiplaescolha_opcao rmo, campus c, categoria cat, opcao o, questao q where r.idResposta = rme.idResposta and rnd.validado = 'Y' and rnd.entrevistado_idEntrevistado = r.entrevistado_idEntrevistado and rmo.respostamultiplaescolha_idResposta = r.idResposta and rnd.questionario_idQuestionario = " + questionario.getIdQuestionario() + " and e.idEntrevistado = rnd.entrevistado_idEntrevistado and r.entrevistado_idEntrevistado = e.idEntrevistado and e.campus_idCampus = " + campus.getIdCampus() + " and e.categoria_idCategoria = " + categoria.getIdCategoria() + " and c.idCampus = e.campus_idCampus and cat.idCategoria = e.categoria_idCategoria and rmo.opcoes_idOpcao = o.idOpcao and r.questao_idQuestao = q.idQuestao";
                    Query querySQLM = session.createSQLQuery(sqlM);
                    List respostasM = querySQLM.list();

                    for (Object linha : respostasM) {
                        Object[] l = (Object[]) linha;
                        gravarArq.print("\""+l[0]+"\";");
                        gravarArq.print("\""+l[1]+"\";");
                        gravarArq.print("\""+l[2]+"\";");
                        gravarArq.print("\""+l[3]+"\";");
                        gravarArq.print("\""+l[4]+"\"\n");
                    }

                    /**
                     * Carregar respostas abertas
                     */
                    String sqlA = "select c.nomecompleto as campus, cat.rotulocompleto as categoria, q.rotulo as rotulo, q.texto as questao, ra.nomecompleto as resposta from resposta r, respostaaberta ra, respondente rnd, entrevistado e, campus c, categoria cat, questao q where r.idResposta = ra.idResposta and rnd.validado = 'Y' and rnd.entrevistado_idEntrevistado = r.entrevistado_idEntrevistado and ra.idResposta = r.idResposta and rnd.questionario_idQuestionario = " + questionario.getIdQuestionario() + " and e.idEntrevistado = rnd.entrevistado_idEntrevistado and r.entrevistado_idEntrevistado = e.idEntrevistado and e.campus_idCampus = " + campus.getIdCampus() + " and e.categoria_idCategoria = " + categoria.getIdCategoria() + " and c.idCampus = e.campus_idCampus and cat.idCategoria = e.categoria_idCategoria and r.questao_idQuestao = q.idQuestao";
                    Query querySQLA = session.createSQLQuery(sqlA);
                    List respostasA = querySQLA.list();
                    for (Object linha : respostasA) {
                        Object[] l = (Object[]) linha;
                        gravarArq.print("\""+l[0]+"\";");
                        gravarArq.print("\""+l[1]+"\";");
                        gravarArq.print("\""+l[2]+"\";");
                        gravarArq.print("\""+l[3]+"\";");
                        gravarArq.print("\""+l[4]+"\"\n");
                    }                    
                }
            }
            arq.close();
            session.getTransaction().commit();
            session.close();
        } catch (IOException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                arq.close();
            } catch (IOException ex) {
            }
        }
    }
   
}