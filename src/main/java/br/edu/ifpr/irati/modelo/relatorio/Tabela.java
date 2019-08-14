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

import java.util.ArrayList;
import java.util.List;

/**
 * Armazena os dados de uma tabela que são utilizados pela interface 
 * para mostrar tabelas e criar gráficos.
 * 
 * @author Valter Estevam
 */
public class Tabela {
    
    /**
     * Título da tabela.
     */
    private String titulo;
    
    /**
     * Listagem de registros da tabela (linhas)
     */
    private List<Registro> registros;
    
    /**
     * Listagem das colunas da tabela (colunas)
     */
    private List<String> colunas;
    
    /**
     * True se for uma tabela criada para uma questão de múltipla escolha.
     */
    private boolean multiplaEscolha;

    /**
     * Construtor padrão.
     */
    public Tabela() {
        titulo = "";
        registros = new ArrayList<>();
        colunas = new ArrayList<>();
        multiplaEscolha = true;
    }

    /**
     * Construtor.
     * 
     * @param titulo título da tabela.
     * @param registros relação de registros.
     * @param colunas relação de colunas.
     */
    public Tabela(String titulo, List<Registro> registros, List<String> colunas) {
        this.titulo = titulo;
        this.registros = registros;
        this.colunas = colunas;
        multiplaEscolha = true;
    }
        
    /**
     * 
     * @return título da tabela.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * 
     * @param titulo título da tabela.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * 
     * @return listagem de registros.
     */
    public List<Registro> getRegistros() {
        return registros;
    }

    /**
     * 
     * @param registros listagem de registros.
     */
    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }

    /**
     * 
     * @return listagem de colunas.
     */
    public List<String> getColunas() {
        return colunas;
    }

    /**
     * 
     * @param colunas listagem de colunas.
     */
    public void setColunas(List<String> colunas) {
        this.colunas = colunas;
    }

    /**
     * 
     * @return true se é uma tabela relacionada a uma questão de múltipla escolha.
     */
    public boolean isMultiplaEscolha() {
        return multiplaEscolha;
    }

    /**
     * 
     * @param multiplaEscolha true se é uma tabela relacionada a uma questão de múltipla escolha.
     */
    public void setMultiplaEscolha(boolean multiplaEscolha) {
        this.multiplaEscolha = multiplaEscolha;
    }
    
}