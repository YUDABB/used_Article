package com.example.Used.Article.controller;

import com.example.Used.Article.domain.Item;
import com.example.Used.Article.domain.Member;
import com.example.Used.Article.service.ItemService;
import com.example.Used.Article.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    // 상품등록
    @GetMapping("/item/new")
    public String createForm(Model model){
        model.addAttribute("form", new ItemForm());
        return "createItemForm";
    }


    @PostMapping("/item/new")
    public String create(@Valid ItemForm itemForm, BindingResult bindingResult, Principal principal, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }
        Member member = this.memberService.getUser(principal.getName());
        this.itemService.createItem(itemForm.getName(), itemForm.getPrice(),itemForm.getDetail(),itemForm.getItem_count(), member, file);
        return "redirect:/";
    }


    // 상품리스트
    @GetMapping("/item")
    public String list(Model model) {
        List<Item> item = itemService.findItems();
        model.addAttribute("item", item);
        return "itemListForm";
    }

    //상품수정
    @GetMapping("item/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);

        ItemForm form = new ItemForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setDetail(item.getDetail());
        form.setItem_count(item.getItem_count());
        form.setImgName(item.getImgName());
        form.setImgPath(item.getImgPath());

        model.addAttribute("form", form);
        return "updateItemForm";
    }

    @PostMapping("item/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") ItemForm form,MultipartFile file)throws Exception{

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getDetail(), form.getItem_count(), file);
        return "redirect:/item";
    }

    // 상품삭제
    @GetMapping("item/{itemId}/delete")
    public String deleteItem(Principal principal, @PathVariable("itemId") Long itemId) {
        Item item = this.itemService.findOne(itemId);
        if (!item.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.itemService.deleteItem(item);
        return "redirect:/myItems";
    }


    //사용자가 등록한 상품
    @GetMapping("/myItems")
    public String myItems(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberService.getUser(username);
        Long memberId = member.getId();
        List<Item> myItems = itemService.findItemsByMember(memberId);
        model.addAttribute("myItems", myItems);
        return "myItemForm";
    }

    // 상품 상세정보
    @GetMapping("item/{itemId}/detail")
    public String createForm(@PathVariable("itemId") Long itemId, Model model) {

        Item item = itemService.findOne(itemId);
        List<Member> members = memberService.findMembers();

        model.addAttribute("item", item);;
        model.addAttribute("members", members);

        return "itemDetailForm";
    }

    // 상품 검색
    @GetMapping("/item/search")
    public String searchItems(@RequestParam("keyword") String keyword, @RequestParam("option") String option, Model model) {
        List<Item> searchResult;
        if ("itemName".equals(option)) {
            searchResult = itemService.findByItemName(keyword);
        } else if ("itemStatus".equals(option)) {
            searchResult = itemService.findByItemStatus(keyword);
        } else {
            throw new IllegalArgumentException("올바르지 않은 검색 옵션입니다.");
        }
        model.addAttribute("searchResult", searchResult);
        return "itemListForm";
    }
}

