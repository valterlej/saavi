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
package br.edu.ifpr.irati.modelo.relatorio;

import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Armazena as configurações para emissão de relatório.
 *
 * @author Valter Estevam
 */
public class ConfiguracaoRelatorio {

    /**
     * Identificadores das categorias selecionadas separadas por vírgula.
     */
    private String categoriasSelecionadas;

    /**
     * Identificadores dos campi selecionados separados por vírgula.
     */
    private String campiSelecionados;

    /**
     * Tipo das tabelas.
     * Valores possíveis: Valores absolutos, Valores percentuais, Ambos ou Não exibir
     */
    private String tipoTabela;

    /**
     * Tipo dos gráficos.
     * Valores possíveis: Valores absolutos, Valores percentuais, Ambos ou Não exibir
     */
    private String tipoGrafico;

    /**
     * Relação de categorias que podiam responder ao questionário.
     */
    private List<Categoria> categorias;

    /**
     * Relação de campus da instituição.
     */
    private List<Campus> campus;

    /**
     * Lista das categorias selecionadas.
     */
    private List<Categoria> categoriasSelecionadasList;

    /**
     * Lista dos campus selecionados.
     */
    private List<Campus> campiSelecionadosList;

    /**
     * Construtor padrão.
     */
    public ConfiguracaoRelatorio() {
        categoriasSelecionadas = "";
        campiSelecionados = "";
        tipoTabela = "";
        tipoGrafico = "";
        categorias = new ArrayList<>();
        campus = new ArrayList<>();
        categoriasSelecionadasList = new ArrayList<>();
        campiSelecionadosList = new ArrayList<>();
    }

    /**
     * Construtor.
     * 
     * @param categorias lista das categorias que podem responder ao questionário.
     * @param campus lista de campus da instituição.
     */
    public ConfiguracaoRelatorio(List<Categoria> categorias, List<Campus> campus) {
        this.categoriasSelecionadas = "";
        this.campiSelecionados = "";
        this.tipoTabela = "";
        this.tipoGrafico = "";
        this.categorias = categorias;
        Collections.sort(categorias);
        this.campus = campus;
        Collections.sort(campus);
        categoriasSelecionadasList = new ArrayList<>();
        campiSelecionadosList = new ArrayList<>();
    }

    /**
     * 
     * @return identificadores das categorias selecionadas separadas por vírgula.
     */
    public String getCategoriasSelecionadas() {
        return categoriasSelecionadas;
    }

    /**
     * 
     * @param categoriasSelecionadas identificadores das categorias selecionadas separadas por vírgula.
     */
    public void setCategoriasSelecionadas(String categoriasSelecionadas) {
        this.categoriasSelecionadas = categoriasSelecionadas;
    }

    /**
     * 
     * @return identificadores dos campus selecionados separaados por vírgula.
     */
    public String getCampiSelecionados() {
        return campiSelecionados;
    }

    /**
     * 
     * @param campiSelecionados identificadores dos campus selecionados por vírgula.
     */
    public void setCampiSelecionados(String campiSelecionados) {
        this.campiSelecionados = campiSelecionados;
    }

    /**
     * 
     * @return tipo selecionado para a tabela.
     */
    public String getTipoTabela() {
        return tipoTabela;
    }

    /**
     * 
     * @param tipoTabela tipo selecionado para a tabela.
     */
    public void setTipoTabela(String tipoTabela) {
        this.tipoTabela = tipoTabela;
    }

    /**
     * 
     * @return tipo selecionado para o gráfico.
     */
    public String getTipoGrafico() {
        return tipoGrafico;
    }

    /**
     * 
     * @param tipoGrafico tipo selecionado para o gráfico.
     */
    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }
 
    /**
     * 
     * @return listagem das categorias vinculadas ao questionário.
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }
    
    /**
     * 
     * @param categorias listagem das categorias vinculadas ao questionário.
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * 
     * @return relação de campus da instituição.
     */
    public List<Campus> getCampus() {
        return campus;
    }

    /**
     * 
     * @param campus relação de campus da instituição.
     */
    public void setCampus(List<Campus> campus) {
        this.campus = campus;
    }
        
    /**
     * Processa a string de identificadores separados por vírgula para 
     * montar a listagem de categorias selecionadas.
     * 
     * @return listagem das categorias selecionadas.
     */
    public List<Categoria> getCategoriasSelecionadasList() {

        this.categoriasSelecionadasList.clear();
        if (!this.categoriasSelecionadas.equals("")) {
            String ids[] = this.categoriasSelecionadas.split(",");
            for (String id : ids) {
                for (Categoria c : categorias) {
                    if (c.getIdCategoria() == Integer.parseInt(id)) {
                        this.categoriasSelecionadasList.add(c);
                    }
                }
            }
        }
        return categoriasSelecionadasList;
    }

    /**
     * Processa a string de identificadores separados por vírgula para 
     * montar a listagem de campi selecionados.
     * 
     * @return listagem dos campi selecionados.
     */
    public List<Campus> getCampiSelecionadosList() {

        this.campiSelecionadosList.clear();
        if (!this.campiSelecionados.equals("")) {
            String ids[] = this.campiSelecionados.split(",");
            for (String id : ids) {
                for (Campus c : campus) {
                    if (c.getIdCampus() == Integer.parseInt(id)) {
                        this.campiSelecionadosList.add(c);
                    }
                }
            }
        }
        return campiSelecionadosList;

    }

}