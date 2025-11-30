package com.cafepos.infra;

import com.cafepos.app.CheckoutService;
import com.cafepos.common.Money;
import com.cafepos.domain.OrderRepository;
import com.cafepos.pricing.FixedCouponDiscount;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.TaxPolicy;

public final class Wiring {

    public static final class Components {
        private final OrderRepository orderRepository;
        private final PricingService pricingService;
        private final CheckoutService checkoutService;

        public Components(OrderRepository orderRepository,
                          PricingService pricingService,
                          CheckoutService checkoutService) {
            this.orderRepository = orderRepository;
            this.pricingService = pricingService;
            this.checkoutService = checkoutService;
        }

        public OrderRepository orderRepository() { return orderRepository; }
        public PricingService pricingService() { return pricingService; }
        public CheckoutService checkoutService() { return checkoutService; }
    }

    private Wiring() { }

    public static Components createDefault() {
        OrderRepository orders = new InMemoryOrderRepository();

        FixedCouponDiscount discountPolicy = new FixedCouponDiscount(Money.zero());

        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Money taxOf(Money amount) {
                return amount.percentage(10);
            }

            @Override
            public Money taxOn(Money discounted) {
                return discounted.percentage(10);
            }
        };

        PricingService pricingService = new PricingService(discountPolicy, taxPolicy);

        CheckoutService checkoutService = new CheckoutService(orders, pricingService);

        return new Components(orders, pricingService, checkoutService);
    }
}
