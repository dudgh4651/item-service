package hello.itemservice.domain.item;

import hello.itemservice.domain.Item.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();   //실제로는 HashMap 사용하면 안된다. 동시에 여러스레드 접근하는경우  ConcurrentHashMap 이걸써야한다.      //static
    private static long sequence = 0L; //static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findall() {
        return  new ArrayList<>(store.values());  //한번 감싸서 보내면 ArrayList를 가지고 값을 변경하니까 실제 store에 영향을 주지않아서 감쌌다
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
