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

import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.relatorio.ConfiguracaoRelatorio;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Gerenciado da tela de relatório.
 *
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class RelatorioMB implements Serializable {

    /**
     * Listagem de todos os questionários.
     */
    private List<Questionario> questionarios;

    /**
     * Questionário selecionado para emissão de relatório.
     */
    private Questionario questionarioSelecionado;

    /**
     * Configurações definidas para o relatório.
     */
    private ConfiguracaoRelatorio configuracaoRelatorio;

    /**
     * Exibir painel de listagem.
     */
    private boolean mostrarListagem;

    /**
     * Mostrar painel de formulário de configurações.
     */
    private boolean mostrarFormulario;

    /**
     * Acesso aos dados de questionário.
     */
    private Dao<Questionario> questionarioDAO;

    /**
     * Construtor padrão.
     */
    public RelatorioMB() {
        questionarioDAO = new GenericDAO<>(Questionario.class);
        mostrarListagem = true;
        mostrarFormulario = false;
        try {
            questionarios = questionarioDAO.buscarTodos(Questionario.class);
        } catch (PersistenceException ex) {
            questionarios = new ArrayList<>();
        }
    }

    /**
     *
     * Selecionar um questionário para emitir o relatório.
     *
     * @param questionario questionário selecionado.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String selecionarQuestionario(Questionario questionario) {
        this.questionarioSelecionado = questionario;
        Dao<Campus> campusDAO = new GenericDAO<>(Campus.class);
        List<Campus> campi;
        try {
            campi = campusDAO.buscarTodos(Campus.class);
        } catch (PersistenceException ex) {
            campi = new ArrayList<>();
        }
        campi.add(new Campus(-1, "Total", "Total"));
        List<Categoria> categorias = new ArrayList<>(this.questionarioSelecionado.getCategorias());
        configuracaoRelatorio = new ConfiguracaoRelatorio(categorias, campi);

        this.mostrarListagem = false;
        this.mostrarFormulario = true;

        return "/administracao/relatorio";

    }

    /**
     *
     * Encaminhar para a tela de validação de respondentes.
     *
     * @param questionario questionário selecionado.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String validarRespondentes(Questionario questionario) {
        this.questionarioSelecionado = questionario;

        //verificar se é a primeira execução ou a segunda
        FacesContext context = FacesContext.getCurrentInstance();
        ValidarRespondentesMB validarRespondentesMB = (ValidarRespondentesMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "validarRespondentesMB");
        if (validarRespondentesMB != null) {
            validarRespondentesMB.inicializar();
        }
        return "/administracao/validarrespondentes";
    }

    /**
     * Cancelar a emissão de relatório.
     *
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String cancelar() {
        configuracaoRelatorio = new ConfiguracaoRelatorio();
        this.mostrarListagem = true;
        this.mostrarFormulario = false;
        return "/administracao/relatorio";
    }

    /**
     * Gerar o relatório com as configurações escolhidas.
     *
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String gerarRelatorio() {

        /**
         * Validar restrições - ao menos um campus e uma categoria devem estar
         * selecionados - não é permitido indicar "Não exibir" tanto para os
         * gráficos quanto para as tabelas.
         *
         */
        boolean validado = true;
        if (this.configuracaoRelatorio.getCampiSelecionadosList().isEmpty()) {
            validado = false;
            Mensagem.mostrar("alerta", "Atenção", "Ao menos um campus deve ser selecionado.");
        }
        if (this.configuracaoRelatorio.getCategoriasSelecionadasList().isEmpty()) {
            validado = false;
            Mensagem.mostrar("alerta", "Atenção", "Ao menos uma categoria deve ser selecionada.");
        }
        if (this.configuracaoRelatorio.getTipoTabela().equals("Não exibir") && this.configuracaoRelatorio.getTipoGrafico().equals("Não exibir")) {
            validado = false;
            Mensagem.mostrar("alerta", "Atenção", "É obrigatório exibir tabelas ou gráficos.");
        }

        if (validado) {
            //verificar se é a primeira execução ou a segunda
            FacesContext context = FacesContext.getCurrentInstance();
            RelatorioViewMB relatorioViewMB = (RelatorioViewMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "relatorioViewMB");
            if (relatorioViewMB != null) {
                relatorioViewMB.inicializarRelatorio();
            }
            return "/administracao/relatorioview";
        } else {
            return "/administracao/relatorio";
        }

    }

    /**
     *
     * Converter um identificador inteiro em string.
     *
     * @param id identificador a ser convertido para string.
     * @return identificador em formato de string.
     */
    public String idString(int id) {
        return String.valueOf(id);
    }

    /**
     *
     * @return listagem de questionários.
     */
    public List<Questionario> getQuestionarios() {
        return questionarios;
    }

    /**
     *
     * @param questionarios listagem de questionários.
     */
    public void setQuestionarios(List<Questionario> questionarios) {
        this.questionarios = questionarios;
    }

    /**
     *
     * @return questionário selecionado para emissão do relatório.
     */
    public Questionario getQuestionarioSelecionado() {
        return questionarioSelecionado;
    }

    /**
     *
     * @param questionarioSelecionado questionário selecionado para emissão do
     * relatório.
     */
    public void setQuestionarioSelecionado(Questionario questionarioSelecionado) {
        this.questionarioSelecionado = questionarioSelecionado;
    }

    /**
     *
     * @return configuração para emissão do relatório.
     */
    public ConfiguracaoRelatorio getConfiguracaoRelatorio() {
        return configuracaoRelatorio;
    }

    /**
     *
     * @param configuracaoRelatorio configuração para emissão do relatório.
     */
    public void setConfiguracaoRelatorio(ConfiguracaoRelatorio configuracaoRelatorio) {
        this.configuracaoRelatorio = configuracaoRelatorio;
    }

    /**
     *
     * @return true se for para mostrar listagem de questionários.
     */
    public boolean isMostrarListagem() {
        return mostrarListagem;
    }

    /**
     *
     * @param mostrarListagem true se for para mostrar a listagem de
     * questionários.
     */
    public void setMostrarListagem(boolean mostrarListagem) {
        this.mostrarListagem = mostrarListagem;
    }

    /**
     *
     * @return true se for para mostrar o formulário de configuração.
     */
    public boolean isMostrarFormulario() {
        return mostrarFormulario;
    }

    /**
     *
     * @param mostrarFormulario true se for para mostrar o formulário de
     * configuração.
     */
    public void setMostrarFormulario(boolean mostrarFormulario) {
        this.mostrarFormulario = mostrarFormulario;
    }

}
