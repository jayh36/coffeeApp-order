package coffeeApp;

import javax.persistence.*;

import coffeeApp.external.PaymentService;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long orderId;
    private Long productId;
    private String price;
    private String status;

    /**
     * 커피주문을 하면 결제완료 후 주문내역 전달
     * */
    @PrePersist
    public void onPrePersist(){

        String checkPayment = OrderApplication.applicationContext.getBean(PaymentService.class).checkPayment(this.getPrice());

        if("결제완료".equals(checkPayment)){
            this.setStatus(checkPayment);
        }else{
            this.setStatus("결제오류");
        }
    }
    /**
     * 결제완료 되면 카페로 주문전달
     * */
    @PostPersist
    public void onPostPersist(){
        OrderRequested orderRequested = new OrderRequested();
        BeanUtils.copyProperties(this, orderRequested);
        if("결제완료".equals(this.getStatus())) orderRequested.publishAfterCommit();
    }
    /**
     * 주문취소 이벤트 전달
     * */
    @PostUpdate
    public  void onPostUpdate(){
        if("주문취소".equals(this.getStatus())){
            OrderCancelRequested orderCancelRequested = new OrderCancelRequested();
            BeanUtils.copyProperties(this, orderCancelRequested);
            orderCancelRequested.publishAfterCommit();
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
