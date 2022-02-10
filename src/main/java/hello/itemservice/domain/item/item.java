package hello.itemservice.domain.Item;


import lombok.Data;

/* @Data를 쓰게되면 아래를 모두 쓸수있게 되는데 이건 위험한거다 그래서 위험한걸 말하기위해 여기서 사용.
  @see Getter
  @see Setter
  @see RequiredArgsConstructor
  @see ToString
  @see EqualsAndHashCode
  @see lombok.Value

********************************************************
  @Getter  @Setter  ==> 이렇게 분리해서 쓰는걸 추찬한다
********************************************************
*/
@Data
public class item {
    private  Long id;
    private  String itemName;
    private  Integer price;    //Integer는 객체라 null이 들어갈수있어서 필수값아닌경우에 쓰려고 만약 Int를 쓰게되면 0이라도 들어가야한다.
    private  Integer quantity;


    //generate 단축키
    /*
    getter/setter/생성자 자동완성 (Generate)
    MacOS: Cmd + n
    Win/Linux: Alt + Insert
    */

    public item() {
    }

    public item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
