package com.sincdor.myshoppinglist;

import java.io.Serializable;

/**
 * Created by andre on 1/26/17.
 */

public class Item implements Serializable {
    private String name;
    private String shopName;
    private String brand;
    private String price;
    private Float quantidade;
    private String observacoes;
    private String unidade;
    private String date;
    private Integer comprado;

    public Item(String name, String shopName, String brand, String price, Float quantidade, String observacoes, String unidade, String date, Integer comprado) {
        this.name = name;
        this.shopName = shopName;
        this.brand = brand;
        this.price = price;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
        this.unidade = unidade;
        this.date = date;
        this.comprado = comprado;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getComprado() {
        return comprado;
    }

    public void setComprado(Integer comprado) {
        this.comprado = comprado;
    }
}
