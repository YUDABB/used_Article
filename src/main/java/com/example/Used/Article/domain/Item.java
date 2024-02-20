package com.example.Used.Article.domain;

import com.example.Used.Article.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "item")
public class Item {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private String detail; // 아이템 상세정보
    private int price;
    private int item_count;

    @ManyToOne
    private Member user;

    // 이미지
    private String imgPath;
    private String imgName;

   // 상품 수량 증가
    public void addStock(int quantity) {
        this.item_count += quantity;
    }

    // 상품 수량 감소
    public void removeStock(int quantity) {
        int restStock = this.item_count - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("상품수량부족");
        }
        this.item_count = restStock;
    }



}
