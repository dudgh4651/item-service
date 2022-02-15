package hello.itemservice.web.basic;

import hello.itemservice.domain.Item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //롬복에 있는 에노테이션인데 이걸쓰면 final이 붙은걸 가지고 생성자 만들어준다
public class BasicItemController {

    private final ItemRepository itemRepository;


    //constructor    alt+insert

    //@Autowired  생성자가 하나만 있으면 생략가능
    /*@RequiredArgsConstructor //롬복에 있는 에노테이션인데 이걸쓰면 final이 붙은걸 가지고 생성자 만들어준다 그러므로 생략가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }*/


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findall();
        model.addAttribute("items",items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }


    //같은 url인데 http 메서드로 기능을 구분한다  (get,post)////
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(
            @RequestParam String itemName,
            @RequestParam int price,
            @RequestParam Integer quantity,
            Model model)
    {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(
            @ModelAttribute("item") Item item, Model model)
            //@ModelAttribute
    {
        /* @ModelAttribute 가 아래내용 자동으로 만들어준다
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        */
        itemRepository.save(item);

        /* @ModelAttribute 가 아래내용 즉 모델에 넣어주는 역할까지 해준다 그래서 생략가능 ////// 그때 이름은 지정해준이름으로   @ModelAttribute("item") 여기서의 item
        model.addAttribute("item",item);
        */

        return "basic/item";
    }

    /*
 @ModelAttribute 기능
   1. 요청 파라미터 처리
    @ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로    입력해준다.

   2. Model 추가
    @ModelAttribute 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute로 지정한 객체를 자동으로 넣어준다.
    지금 코드를 보면 model.addAttribute("item", item) 가 주석처리되어 있어도 잘 동작하는 것을 확인할 수 있다.
    모델에 데이터를 담을 때는 이름이 필요하다. 이름은 @ModelAttribute 에 지정한 name(value) 속성을 사용한다.
    만약 다음과 같이 @ModelAttribute 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.

    @ModelAttribute("hello") Item item 이름을 hello 로 지정
    model.addAttribute("hello", item); 모델에 hello 이름으로 저장
*/



    //@PostMapping("/add")
    public String addItemV3(
            @ModelAttribute Item item)  {
        //@ModelAttribute("item") 여기에서 ("item")제거하면?!
        //클래서명 첫글자 소문자로 바뀌고 아이템명이 된다. 그이름으로 모델 어트리뷰트에 들어간다.
        //ex Item -> item
        //ex HellData -> helloData
        itemRepository.save(item);
        return "basic/item";
    }



    //비추
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }


    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId() ;
        /*
        item.getId() 처럼 URL에 변수를 더해서 사용하는 것은
        URL 인코딩이 안되기 때문에 위험하다.
        RedirectAttributes를 사용하자.
        */

    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}" ;

        //return "redirect:/basic/items/{itemId}" ; 치환에 포함되지않은 변수는 쿼리 파라미터에 포함된다.
        //http://localhost:8080/basic/items/3?status=true

        //RedirectAttributes
        //RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible , 쿼리 파라미터까지 처리해준다.

    }

    //////////////////////////////////////////////////////










    //////////////////////////////////////////////////////

    //GET /items/{itemId}/edit : 상품 수정 폼
    //POST /items/{itemId}/edit : 상품 수정 처리

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,  @ModelAttribute Item item ){
        itemRepository.update( itemId, item );
        return "redirect:/basic/items/{itemId}"; //redirect방법 여기에서 @PathVariable 값 쓸수있다.
    }

    /*
    **리다이렉트
    상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.

    * 스프링은 redirect:/... 으로 편리하게 리다이렉트를 지원한다.

    * redirect:/basic/items/{itemId}"
       컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
       redirect:/basic/items/{itemId} -> {itemId} 는 @PathVariable Long itemId 의 값을 그대로 사용한다.
    */


    /*
    참고
     > HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용할 수 있다.
     > PUT, PATCH는 HTTP API 전송시에 사용
     > 스프링에서 HTTP POST로 Form 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청상 POST 요청이다.   -> 거의 사용안함
    */
    //////////////////////////////////////////////////////





    /**
     *테스트용 데이터를 추가  확인을 위해
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item(("itemA"), 10000, 10));
        itemRepository.save(new Item(("itemB"), 20000, 20));

    }


}
