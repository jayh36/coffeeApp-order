package coffeeApp;

import coffeeApp.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationAccepted_StatusChange(@Payload OrderAccepted orderAccepted){

        if(orderAccepted.isMe()){
            System.out.println("##### listener StatusChange : " + orderAccepted.toJson());

            Optional<Order> optionalOrder = orderRepository.findById(orderAccepted.getOrderId());
            Order order = optionalOrder.orElseGet(Order::new);
            //Reservation reservation = new Reservation() ;
            order.setStatus(orderAccepted.getStatus());
            orderRepository.save(order);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationCanceled_StatusChange(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            System.out.println("##### listener StatusChange : " + orderCanceled.toJson());
            Optional<Order> optionalOrder = orderRepository.findById(orderCanceled.getOrderID());
            Order order = optionalOrder.orElseGet(Order::new);
            //Reservation reservation = new Reservation() ;
            order.setOrderId(orderCanceled.getOrderID());
            order.setStatus(orderCanceled.getStatus());
            orderRepository.save(order);
        }
    }

}
