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
package br.edu.ifpr.irati.mb.administracao;

import br.edu.ifpr.irati.dao.RelatorioDAO;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.relatorio.ConfiguracaoRelatorio;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.QuestaoMultiplaEscolha;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.relatorio.Registro;
import br.edu.ifpr.irati.modelo.relatorio.RegistroMultiplaEscolha;
import br.edu.ifpr.irati.modelo.relatorio.Tabela;
import br.edu.ifpr.irati.mb.util.Path;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LegendPlacement;

/**
 *
 * Controla a exibição do relatório.
 *
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class RelatorioViewMB implements Serializable {

    /**
     * Questão atual no paginador
     */
    private Questao questaoAtual;

    /**
     * Questionário para o qual o relatório foi elaborado
     */
    private Questionario questionario;

    /**
     * Configurações marcadas pelo usuário para o relatório
     */
    private ConfiguracaoRelatorio configuracaoRelatorio;

    /**
     * Lista de tabelas. Utilizada para construir as tabelas na interface
     * gráfica
     */
    private List<Tabela> tabelas;

    /**
     * Mapa para relacionar tabela com questões
     */
    private Map<Questao, List<Tabela>> mapaTabelas;

    /**
     * Relação de questões do questionário ordenadas pelo atributo ordem
     */
    private List<Questao> questoes;

    /**
     * Lista das posições das questões utilizada para dar funcionalidade aos
     * botões de acesso direto às questões.
     */
    private List<Integer> botoes;

    /**
     * Mapeamento entre tabela e gráfico
     */
    private Map<Tabela, List<HorizontalBarChartModel>> mapaGraficos;

    /**
     * Link para o arquivo de exportação
     */
    private String linkExportacao;

    /**
     * Atributo para controlar a exibição do botão anterior.
     */
    private boolean mostrarAnterior;

    /**
     * Atributo para controlar a exibição do botão próximo.
     */
    private boolean mostrarProximo;

    /**
     * Atributo para controlar a exibição de tabelas.
     */
    private boolean mostrarTabela;

    /**
     * Atributo para controlar a exibição de tabelas de valores absolutos.
     */
    private boolean tabelaValoresAbsolutos;

    /**
     * Atributo para controlar a exibição de tabelas de valores percentuais.
     */
    private boolean tabelaValoresPercentuais;

    /**
     * Atributo para controlar a exibição de gráficos.
     */
    private boolean mostrarGrafico;

    /**
     * Atributo para controlar a exibição de gráficos de valores absolutos.
     */
    private boolean graficoValoresAbsolutos;

    /**
     * Atributo para controlar a exibição de gráfico de valores percentuais.
     */
    private boolean graficoValoresPercentuais;

    /**
     * Objeto para acesso aos dados do relatório.
     */
    private RelatorioDAO relatorioDAO;

    /**
     * Construtor padrão.
     */
    public RelatorioViewMB() {
        inicializarRelatorio();
    }

    /**
     * Inicializa o relatório obtendo valores em outros controladores e buscando
     * os dados no banco de dados.
     */
    public void inicializarRelatorio() {
        /**
         * Instanciando os atributos.
         */
        questaoAtual = new Questao();
        questionario = new Questionario();
        configuracaoRelatorio = new ConfiguracaoRelatorio();
        tabelas = new ArrayList<>();
        mapaTabelas = new HashMap<>();
        questoes = new ArrayList<>();
        mapaGraficos = new HashMap<>();
        botoes = new ArrayList<>();
        mostrarAnterior = false;
        mostrarProximo = true;
        linkExportacao = "";
        relatorioDAO = new RelatorioDAO();

        /**
         * Obtendo o questionário selecionado (e as questões) e a configuração
         * do relatório.
         */
        FacesContext context = FacesContext.getCurrentInstance();
        RelatorioMB relatorioMB = (RelatorioMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "relatorioMB");
        if (relatorioMB != null) {
            questionario = relatorioMB.getQuestionarioSelecionado();
            questoes = new ArrayList<>(questionario.getQuestoes());
            Collections.sort(questoes);
            this.configuracaoRelatorio = relatorioMB.getConfiguracaoRelatorio();
            switch (this.configuracaoRelatorio.getTipoTabela()) {
                case "Valores absolutos":
                    this.mostrarTabela = true;
                    this.tabelaValoresAbsolutos = true;
                    this.tabelaValoresPercentuais = false;
                    break;
                case "Valores percentuais":
                    this.mostrarTabela = true;
                    this.tabelaValoresAbsolutos = false;
                    this.tabelaValoresPercentuais = true;
                    break;
                case "Ambos":
                    this.mostrarTabela = true;
                    this.tabelaValoresAbsolutos = true;
                    this.tabelaValoresPercentuais = true;
                    break;
                default:
                    this.mostrarTabela = false;
                    this.tabelaValoresAbsolutos = false;
                    this.tabelaValoresPercentuais = false;
                    break;
            }

            switch (this.configuracaoRelatorio.getTipoGrafico()) {
                case "Valores absolutos":
                    this.mostrarGrafico = true;
                    this.graficoValoresAbsolutos = true;
                    this.graficoValoresPercentuais = false;
                    break;
                case "Valores percentuais":
                    this.mostrarGrafico = true;
                    this.graficoValoresAbsolutos = false;
                    this.graficoValoresPercentuais = true;
                    break;
                case "Ambos":
                    this.mostrarGrafico = true;
                    this.graficoValoresAbsolutos = true;
                    this.graficoValoresPercentuais = true;
                    break;
                default:
                    this.mostrarGrafico = false;
                    this.graficoValoresAbsolutos = false;
                    this.graficoValoresPercentuais = false;
                    break;
            }
        }

        /**
         * Obtendo categorias e campus selecionados
         */
        List<Categoria> categoriasSelecionadas = new ArrayList<>(configuracaoRelatorio.getCategoriasSelecionadasList());
        Collections.sort(categoriasSelecionadas);
        List<Campus> campiSelecionados = new ArrayList<>(configuracaoRelatorio.getCampiSelecionadosList());
        Collections.sort(campiSelecionados);

        /**
         * Tabelas e gráficos de resumo
         */
        Tabela quadroRespondentes = relatorioDAO.criarTabelaResumo(questionario, categoriasSelecionadas, campiSelecionados);
        QuestaoMultiplaEscolha qmeResumo = new QuestaoMultiplaEscolha();
        qmeResumo.setOrdem(0);
        qmeResumo.setTexto("Resumo dos entrevistados");
        this.questoes.add(qmeResumo);
        Collections.sort(this.questoes);

        for (Questao q : questoes) {
            botoes.add(q.getOrdem());
        }
        List<Tabela> tabelasResumo = new ArrayList<>();
        tabelasResumo.add(quadroRespondentes);
        mapaTabelas.put(qmeResumo, tabelasResumo);
        HorizontalBarChartModel model1 = this.criarGrafico(quadroRespondentes, true, "Número de entrevistados", "Campus");
        HorizontalBarChartModel model2 = this.criarGrafico(quadroRespondentes, false, "% de entrevistados", "Campus");
        List<HorizontalBarChartModel> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        mapaGraficos.put(quadroRespondentes, models);

        /**
         * Criar dados para exportação
         */
        this.linkExportacao = Path.obterCaminhoServidor() + "administracao/dados.csv";
        relatorioDAO.exportarDadosEmArquivo(questionario, campiSelecionados, categoriasSelecionadas, linkExportacao);

        /*
        Definir questão atual e listagem de tabelas inicial
         */
        if (!this.questoes.isEmpty()) {
            questaoAtual = this.questoes.get(0);
            tabelas = mapaTabelas.get(questaoAtual);
            this.mostrarAnterior = false;
            this.mostrarProximo = true;
        }
    }

    /**
     * Dada uma tabela cria um gráfico correspondente.
     *
     * @param tabela dados do relatório.
     * @param absoluto informa se é um gráfico com valores absolutos ou
     * relativos.
     * @param rotuloEixoX texto exibido no eixo X.
     * @param rotuloEixoY texto exibido no eixo Y.
     * @return modelo utilizado pelo primefaces para renderizar um gráfico em
     * tela.
     */
    public HorizontalBarChartModel criarGrafico(Tabela tabela, boolean absoluto, String rotuloEixoX, String rotuloEixoY) {

        HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();

        List<ChartSeries> series = new ArrayList<>();
        for (String s : tabela.getColunas()) {
            ChartSeries serie = new ChartSeries();
            serie.setLabel(s);
            series.add(serie);
        }

        //para cada série
        for (int i = 0; i < tabela.getColunas().size(); i++) {

            String s = tabela.getColunas().get(i);

            for (Registro registro : tabela.getRegistros()) {

                /**
                 * Remove a totalização de respostas por categoria
                 */
                if (registro.getTitulo().equals("Total") && tabela.getTitulo().equals("Resumo dos entrevistados")) {
                    continue;
                }

                RegistroMultiplaEscolha rme = (RegistroMultiplaEscolha) registro;

                if (absoluto) {

                    String categoria = rme.getTitulo();
                    int valor = rme.getValoresAbsolutos().get(s);
                    series.get(i).set(categoria, valor);

                } else {

                    String categoria = rme.getTitulo();
                    double valor = rme.getValoresPercentuais().get(s);
                    series.get(i).set(categoria, valor);
                }

            }

        }

        for (ChartSeries serie : series) {
            horizontalBarModel.addSeries(serie);
        }

        horizontalBarModel.setTitle(tabela.getTitulo());
        horizontalBarModel.setLegendPosition("ne");
        horizontalBarModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setShowDatatip(false);

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel(rotuloEixoX);

        xAxis.setMin(0);
        if (!absoluto) {
            xAxis.setMax(100);
        }

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel(rotuloEixoY);

        return horizontalBarModel;

    }

    /**
     * Retorna o texto a ser exibido na primeira coluna das tabelas. Pode ser
     * campus ou categoria dependendo do caso.
     *
     * @param tabela objeto com os dados a serem colocados na tabela na
     * interface gráfica.
     * @return texto a ser exibido.
     */
    public String obterColunaInicialTabela(Tabela tabela) {
        if (tabela.getTitulo().equals("Resumo dos entrevistados")) {
            return "Campus";
        } else {
            return "Categoria";
        }
    }

    /**
     * Retorna um texto para ser inserido como rótulo dos botões de navegação
     * direta entre as questões.
     *
     * @param pos ordem da questão.
     * @return texto a ser exibido.
     */
    public String getValorBotao(int pos) {
        if (pos == 0) {
            return "R";
        } else {
            return String.valueOf(pos);
        }
    }

    /**
     * Retorna a estilização css a ser utilizada na tabela em função da
     * quantidade de dados a serem exibidos.
     *
     * @param tabela dados correspondentes ao gráfico.
     * @return texto com o código css.
     */
    public String estiloGrafico(Tabela tabela) {
        int qtdRegistros = tabela.getRegistros().size();
        String comprimento = "width: 15cm;";
        String margem = "margin: 0 auto;";
        String altura = "height: ";
        if (qtdRegistros <= 12) {
            altura += String.valueOf(300) + "px;";
        } else {
            altura += String.valueOf(qtdRegistros * 25) + "px;";
        }

        return comprimento + " " + margem + " " + altura;
    }

    /**
     * Se a questão da posição informada estiver sendo exibida o botão fica
     * vermelho, caso contrário fica azul.
     *
     * @param pos ordem da questão em exibição.
     * @return estilo do botão.
     */
    public String obterEstiloBotao(int pos) {

        if (this.questoes.get(pos).getIdQuestao() == this.questaoAtual.getIdQuestao()) {
            return "danger";
        } else {
            return "primary";
        }
    }

    /**
     * Dada uma tabela retorna o modelo de gráfico correspondente.
     *
     * @param tabela dados usados no gráfico
     * @param absoluto define se serão usados dados absolutos ou percentuais.
     * @return modelo de gráfico utilizado pelo primefaces para renderização em
     * tela.
     */
    public HorizontalBarChartModel obterModelo(Tabela tabela, boolean absoluto) {
        List<HorizontalBarChartModel> models = mapaGraficos.get(tabela);
        if (absoluto) {
            return models.get(0);
        } else {
            return models.get(1);
        }
    }

    /**
     * Cria as tabelas e gráficos correspondentes à questão dada como parâmetro.
     *
     * @param questao questão para para gerar as tabelas e gráficos.
     * @return listagem de tabelas.
     */
    public List<Tabela> carregarTabelas(Questao questao) {
        List<Tabela> tabelas = relatorioDAO.criarTabelasPorQuestao(questionario, questao, this.configuracaoRelatorio.getCampiSelecionadosList(), this.configuracaoRelatorio.getCategoriasSelecionadasList());

        for (Tabela tabela : tabelas) {
            if (tabela.isMultiplaEscolha()) {
                HorizontalBarChartModel model1 = this.criarGrafico(tabela, true, "Número de entrevistados", "Campus");
                HorizontalBarChartModel model2 = this.criarGrafico(tabela, false, "% de entrevistados", "Campus");
                List<HorizontalBarChartModel> models = new ArrayList<>();
                models.add(model1);
                models.add(model2);
                mapaGraficos.put(tabela, models);
            }
        }
        return tabelas;
    }

    /**
     * Altera a questão atual avançando na ordem das questões do questionário.
     *
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String avancar() {

        int i = this.questoes.indexOf(this.questaoAtual);
        if (i < this.questoes.size() - 1) {
            questaoAtual = this.questoes.get(++i);

            if (mapaTabelas.get(this.questaoAtual) != null && !mapaTabelas.get(this.questaoAtual).isEmpty()) {
                tabelas = mapaTabelas.get(this.questaoAtual);
            } else {
                mapaTabelas.put(questaoAtual, carregarTabelas(questaoAtual));
                tabelas = mapaTabelas.get(this.questaoAtual);
            }
            if (i == this.questoes.size() - 1) {
                this.mostrarAnterior = true;
                this.mostrarProximo = false;
            } else {
                this.mostrarAnterior = true;
                this.mostrarProximo = true;
            }
        }
        return "/administracao/relatorioview";

    }

    /**
     * Altera a questão atual retornando na ordem das questões do questionário.
     *
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String retornar() {

        int i = this.questoes.indexOf(this.questaoAtual);
        if (i > 0) {
            questaoAtual = this.questoes.get(--i);

            if (mapaTabelas.get(this.questaoAtual) != null && !mapaTabelas.get(this.questaoAtual).isEmpty()) {
                tabelas = mapaTabelas.get(this.questaoAtual);
            } else {
                mapaTabelas.put(questaoAtual, carregarTabelas(questaoAtual));
                tabelas = mapaTabelas.get(this.questaoAtual);
            }

            if (i == 0) {
                this.mostrarAnterior = false;
                this.mostrarProximo = true;
            } else {
                this.mostrarAnterior = true;
                this.mostrarProximo = true;
            }
        }
        return "/administracao/relatorioview";

    }

    /**
     * Navegar para a questão da posição informada
     *
     * @param pos ordem da questão para a qual o relatório deve ser
     * redirecionado.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String irPara(int pos) {

        this.questaoAtual = this.questoes.get(pos);
        if (mapaTabelas.get(this.questaoAtual) != null && !mapaTabelas.get(this.questaoAtual).isEmpty()) {
            tabelas = mapaTabelas.get(this.questaoAtual);
        } else {
            mapaTabelas.put(questaoAtual, carregarTabelas(questaoAtual));
            tabelas = mapaTabelas.get(this.questaoAtual);
        }

        int i = this.questoes.indexOf(this.questaoAtual);
        if (i == 0) {
            this.mostrarAnterior = false;
            this.mostrarProximo = true;
        } else {
            this.mostrarAnterior = true;
            this.mostrarProximo = true;
        }
        if (i == this.questoes.size() - 1) {
            this.mostrarAnterior = true;
            this.mostrarProximo = false;
        } else {
            this.mostrarAnterior = true;
            this.mostrarProximo = true;
        }
        return "/administracao/relatorioview";
    }

    /**
     * Retorna o título do painel como a concatenação do nome curto do campus e
     * do texto da questão.
     *
     * @param campus campus informado
     * @param questao questão atual.
     * @return texto a ser exibido.
     */
    public String obterTituloPainel(Campus campus, Questao questao) {
        return campus.getNomeCurto() + " - " + questao.getTexto();
    }

    /**
     *
     * @return listagem de questões.
     */
    public List<Questao> getQuestoes() {
        return questoes;
    }

    /**
     *
     * @param questoes listagem de questões.
     */
    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    /**
     *
     * @return questionário atual.
     */
    public Questionario getQuestionario() {
        return questionario;
    }

    /**
     *
     * @param questionario questionário atual.
     */
    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    /**
     *
     * @return configurações para emissão do relatório.
     */
    public ConfiguracaoRelatorio getConfiguracaoRelatorio() {
        return configuracaoRelatorio;
    }

    /**
     *
     * @param configuracaoRelatorio configurações para emissão do relatório.
     */
    public void setConfiguracaoRelatorio(ConfiguracaoRelatorio configuracaoRelatorio) {
        this.configuracaoRelatorio = configuracaoRelatorio;
    }

    /**
     *
     * @return questão atual em exibição.
     */
    public Questao getQuestaoAtual() {
        return questaoAtual;
    }

    /**
     *
     * @param questaoAtual questão atual em exibição.
     */
    public void setQuestaoAtual(Questao questaoAtual) {
        this.questaoAtual = questaoAtual;
    }

    /**
     *
     * @return listagem de tabelas correspondentes à questão atual.
     */
    public List<Tabela> getTabelas() {
        return tabelas;
    }

    /**
     *
     * @param tabelas listagem de tabelas correspondentes à questão atual.
     */
    public void setTabelas(List<Tabela> tabelas) {
        this.tabelas = tabelas;
    }

    /**
     *
     * @return true se é possível mostrar a questão anterior
     */
    public boolean isMostrarAnterior() {
        return mostrarAnterior;
    }

    /**
     *
     * @param mostrarAnterior true se é possível mostrar a questão anterior.
     */
    public void setMostrarAnterior(boolean mostrarAnterior) {
        this.mostrarAnterior = mostrarAnterior;
    }

    /**
     *
     * @return true se é possível mostrar a próxima questão.
     */
    public boolean isMostrarProximo() {
        return mostrarProximo;
    }

    /**
     *
     * @param mostrarProximo true se é possível mostrar a próxima questão.
     */
    public void setMostrarProximo(boolean mostrarProximo) {
        this.mostrarProximo = mostrarProximo;
    }

    /**
     *
     * @return true se é uma tabela de valores absolutos.
     */
    public boolean isTabelaValoresAbsolutos() {
        return tabelaValoresAbsolutos;
    }

    /**
     *
     * @param tabelaValoresAbsolutos true se é uma tabela de valores absolutos.
     */
    public void setTabelaValoresAbsolutos(boolean tabelaValoresAbsolutos) {
        this.tabelaValoresAbsolutos = tabelaValoresAbsolutos;
    }

    /**
     *
     * @return true se é um gráfico de valores absolutos.
     */
    public boolean isGraficoValoresAbsolutos() {
        return graficoValoresAbsolutos;
    }

    /**
     *
     * @param graficoValoresAbsolutos true se é um gráfico de valores absolutos.
     */
    public void setGraficoValoresAbsolutos(boolean graficoValoresAbsolutos) {
        this.graficoValoresAbsolutos = graficoValoresAbsolutos;
    }

    /**
     *
     * @return true se é para mostrar tabela.
     */
    public boolean isMostrarTabela() {
        return mostrarTabela;
    }

    /**
     *
     * @param mostrarTabela true se é para mostrar tabela.
     */
    public void setMostrarTabela(boolean mostrarTabela) {
        this.mostrarTabela = mostrarTabela;
    }

    /**
     *
     * @return true se é para mostrar gráfico.
     */
    public boolean isMostrarGrafico() {
        return mostrarGrafico;
    }

    /**
     *
     * @param mostrarGrafico true se é para mostrar gráfico.
     */
    public void setMostrarGrafico(boolean mostrarGrafico) {
        this.mostrarGrafico = mostrarGrafico;
    }

    /**
     *
     * @return true se é para mostrar tabela de valores percentuais.
     */
    public boolean isTabelaValoresPercentuais() {
        return tabelaValoresPercentuais;
    }

    /**
     *
     * @param tabelaValoresPercentuais true se é para mostrar tabela de valores
     * percentuais.
     */
    public void setTabelaValoresPercentuais(boolean tabelaValoresPercentuais) {
        this.tabelaValoresPercentuais = tabelaValoresPercentuais;
    }

    /**
     *
     * @return true se é para mostrar gráfico de valores percentuais.
     */
    public boolean isGraficoValoresPercentuais() {
        return graficoValoresPercentuais;
    }

    /**
     *
     * @param graficoValoresPercentuais true se é para mostrar gráfico de
     * valores percentuais.
     */
    public void setGraficoValoresPercentuais(boolean graficoValoresPercentuais) {
        this.graficoValoresPercentuais = graficoValoresPercentuais;
    }

    /**
     *
     * @return mapa de gráficos em função de tabelas correspondentes.
     */
    public Map<Tabela, List<HorizontalBarChartModel>> getMapaGraficos() {
        return mapaGraficos;
    }

    /**
     *
     * @param mapaGraficos mapa de gráficos em função de tabelas
     * correspondentes.
     */
    public void setMapaGraficos(Map<Tabela, List<HorizontalBarChartModel>> mapaGraficos) {
        this.mapaGraficos = mapaGraficos;
    }

    /**
     *
     * @return link para armazenar os dados de exportação.
     */
    public String getLinkExportacao() {
        return linkExportacao;
    }

    /**
     *
     * @param linkExportacao link para armazenar os dados de exportação.
     */
    public void setLinkExportacao(String linkExportacao) {
        this.linkExportacao = linkExportacao;
    }

    /**
     *
     * @return mapa de tabelas em função de questões.
     */
    public Map<Questao, List<Tabela>> getMapaTabelas() {
        return mapaTabelas;
    }

    /**
     *
     * @param mapaTabelas mapa de tabelas em função de questões.
     */
    public void setMapaTabelas(Map<Questao, List<Tabela>> mapaTabelas) {
        this.mapaTabelas = mapaTabelas;
    }

    /**
     *
     * @return ordem dos botões de navegação direta.
     */
    public List<Integer> getBotoes() {
        return botoes;
    }

    /**
     *
     * @param botoes ordem dos botões de navegação direta.
     */
    public void setBotoes(List<Integer> botoes) {
        this.botoes = botoes;
    }

}