package com.example.Used.Article.service;

import com.example.Used.Article.domain.Item;
import com.example.Used.Article.domain.Member;
import com.example.Used.Article.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    //상품 생성
    @Transactional
    public void createItem( String name, int price, String detail, int item_count, Member user, MultipartFile file) throws Exception{
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setDetail(detail);
        item.setItem_count(item_count);
        item.setUser(user);

        if (file != null && !file.isEmpty()) {
            String path = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();
            String imgName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(path, imgName);
            file.transferTo(saveFile);
            item.setImgName(imgName);
            item.setImgPath("/files/" + imgName);
        } else {

            String defaultImageName = "main.jpg"; // 기본 이미지 파일의 이름
            String defaultImagePath = "/src/main/resources/static/files/" + defaultImageName; // 기본 이미지 파일의 경로
            item.setImgName(defaultImageName);
            item.setImgPath(defaultImagePath);

        }
        itemRepository.save(item);
    }

    // 상품 수정
    @Transactional
    public void updateItem(Long id, String name, int price, String detail, int item_count, MultipartFile file) throws Exception{
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
        item.setDetail(detail);
        item.setItem_count(item_count);

        if (file != null && !file.isEmpty()) {
            String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            String imgName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File uploadFile = new File(uploadPath, imgName);

            try {
                file.transferTo(uploadFile);
                if (item.getImgName() != null) { // 이전 이미지 삭제
                    File oldFile = new File(uploadPath, item.getImgName());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                item.setImgName(imgName);  // 새로운 이미지 등록
                item.setImgPath("/files/" + imgName);
            } catch (IOException e) {

                throw new IOException("Failed to upload image", e);
            }
        }
        itemRepository.save(item);

    }


    // 상품명으로 검색
    public List<Item> findByItemName(String keyword) {
        return itemRepository.findByItemName(keyword);
    }

    // 상품 정보로 검색
    public List<Item> findByItemStatus(String keyword) {
        return itemRepository.findByItemStatus(keyword);
    }

    // 상품 삭제
    @Transactional
    public void deleteItem(Item item) { itemRepository.deleteItem(item); }

    // 상품 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItemsByMember(Long memberId) {
        return itemRepository.findByMemberId(memberId);
    }
}
