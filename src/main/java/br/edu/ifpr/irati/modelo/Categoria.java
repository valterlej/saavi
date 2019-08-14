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
package br.edu.ifpr.irati.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * Categoria do entrevistado.
 *
 * @author Valter Estevam
 */
@Entity (name="categoria")
public class Categoria implements Serializable, Comparable<Categoria>{
 
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idCategoria;
    
    /**
     * Rótulo completo da categoria.
     */
    @NotNull(message = "Rótulo completo é obrigatório.")
    @Size(min=1, max=100, message = "O rótulo completo deve possuir entre 1 e 100 caracteres.")  
    @Column(name = "rotulocompleto", nullable = false, length = 100)
    private String rotuloCompleto;
    
    /**
     * Rótulo curto ou sigla para uso em relatórios.
     */
    @NotNull(message = "Rótulo curto é obrigatório.")
    @Size(min=1, max=10, message = "A sigla deve possuir entre 1 e 10 caracteres.") 
    @Column(name = "rotulocurto", nullable = false, length = 10)
    private String rotuloCurto;

    /**
     * Construtor padrão.
     */
    public Categoria() {
        idCategoria = 0;
        rotuloCompleto = "";
        rotuloCurto = "";
    }

    /**
     * Construtor.
     * 
     * @param idCategoria identificador único da categoria.
     * @param rotuloCompleto rótulo completo da categoria.
     * @param rotuloCurto rótulo curto ou sigla para uso em relatórios.
     */
    public Categoria(int idCategoria, String rotuloCompleto, String rotuloCurto) {
        this.idCategoria = idCategoria;
        this.rotuloCompleto = rotuloCompleto;
        this.rotuloCurto = rotuloCurto;
    }    
    
    /**
     * 
     * @return identificador da categoria.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * 
     * @param idCategoria identificador da categoria.
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * 
     * @return rótulo completo da categoria.
     */
    public String getRotuloCompleto() {
        return rotuloCompleto;
    }

    /**
     * 
     * @param rotuloCompleto rótulo curto ou sigla da categoria utilizado em relatórios.
     */
    public void setRotuloCompleto(String rotuloCompleto) {
        this.rotuloCompleto = rotuloCompleto;
    }

    /**
     * 
     * @return rótulo curto ou sigla da categoria utilizado em relatórios.
     */
    public String getRotuloCurto() {
        return rotuloCurto;
    }

    /**
     * 
     * @param rotuloCurto rótulo curto ou sigla da categoria utilizado em relatórios.
     */
    public void setRotuloCurto(String rotuloCurto) {
        this.rotuloCurto = rotuloCurto;
    }
    
    /**
     * Método usado para verificar a igualdade entre objetos.
     * A comparação é feita pelo id.
     * 
     * @param obj objeto a ser comparado.
     * @return true se forem iguais.
     */
    @Override
    public boolean equals(Object obj) {
        Categoria c = (Categoria) obj;
        if (this.idCategoria == c.getIdCategoria()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Método utilizado para comparação entre entre objetos para fins de ordenação.
     * A comparação se dá pelo rótulo completo.
     * 
     * @param o objeto a ser comparado.
     * @return 0 se forem iguais, menor que 0 se o atual é vem antes do comparado e maior do que zero de o objeto comparado vem antes do atual.
     */
    @Override
    public int compareTo(Categoria o) {        
        return this.getRotuloCompleto().compareTo(o.getRotuloCompleto());        
    }
               
}
