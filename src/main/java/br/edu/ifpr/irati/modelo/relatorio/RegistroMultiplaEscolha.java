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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registro em uma tabela correspondente a uma questão de múltipla escolha.
 *
 * @author Valter Estevam
 */
public class RegistroMultiplaEscolha extends Registro{
    
    /**
     * Mapa relacionando o nome das colunas aos valores inteiros do registro.
     */
    private Map<String, Integer> valoresAbsolutos;
    
    /**
     * Mapa relacionando o nome das colunas aos valores percentuais do registro.
     */
    private Map<String, Double> valoresPercentuais;

    /**
     * Construtor padrão.
     */
    public RegistroMultiplaEscolha() {
        super();
        valoresAbsolutos = new HashMap<>();
        valoresPercentuais = new HashMap<>();
    }

    /**
     * Construtor.
     * 
     * @param valores mapa associando valores inteiros às colunas da tabela.
     */
    public RegistroMultiplaEscolha(Map<String, Integer> valores) {
        super();
        this.valoresAbsolutos = valores;
    }

    /**
     * Construtor.
     * 
     * @param titulo título do registro (corresponde à primeira coluna).
     * @param valores valores inteiros associados com cada coluna da tabela.
     */
    public RegistroMultiplaEscolha(String titulo, Map<String, Integer> valores) {
        super(titulo);
        this.valoresAbsolutos = valores;
    }
     
    /**
     * Dada uma coluna retorna o valor absoluto associado a ela.
     * @param coluna coluna desejada.
     * @return valor inteiro convertido em uma String.
     */
    public String valorAbsolutoPorColuna(String coluna){
        return String.valueOf(valoresAbsolutos.get(coluna));
    }
    
    /**
     * Dada uma coluna retorna o valor percentual associado a ela com duas casas de precisão.
     * @param coluna coluna desejada.
     * @return valor de ponto flutuante convertido em uma String.
     */
    public String valorPercentualPorColuna(String coluna){
        return String.format("%.2f", valoresPercentuais.get(coluna));
    }
    
    /**
     * 
     * @return mapa de valores percentuais por coluna.
     */
    public Map<String, Double> getValoresPercentuais() {
        return valoresPercentuais;
    }

    /**
     * 
     * @param valoresPercentuais mapa de valores percentuais por coluna.
     */
    public void setValoresPercentuais(Map<String, Double> valoresPercentuais) {
        this.valoresPercentuais = valoresPercentuais;
    }

    /**
     * 
     * @return mapa de valores absolutos por coluna.
     */
    public Map<String, Integer> getValoresAbsolutos() {
        return valoresAbsolutos;
    }

    /**
     * Quando atribuído o mapa os valores percentuais são calculados e 
     * armazenados no mapa correspondente.
     * 
     * @param valoresAbsolutos mapa de valores absolutos por coluna.
     */
    public void setValoresAbsolutos(Map<String, Integer> valoresAbsolutos) {
        this.valoresAbsolutos = valoresAbsolutos;
        /**
         * Calcular os valores percentuais
         */
        Set<String> keys = this.valoresAbsolutos.keySet();
        double soma = 0;
        for (String k: keys){
            soma += valoresAbsolutos.get(k);            
        }
        if (soma == 0){
            soma = 1.0;
        }
        for (String k: keys){
            valoresPercentuais.put(k,100 * valoresAbsolutos.get(k) / soma);            
        }                        
    }
    
}