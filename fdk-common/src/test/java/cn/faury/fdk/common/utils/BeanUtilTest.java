package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.Properties;
import org.junit.Test;

public class BeanUtilTest {
    @Test
    public void initFields() throws Exception {
        PropertiesUtil pu = PropertiesUtil.createPropertyInstance("bean.properties");
        System.out.println("properties:" + JsonUtil.objectToJson(pu));
        Bean bean = new Bean();
        System.out.println(bean);
        BeanUtil.initFields(bean, pu);
        System.out.println(bean);
    }
    @Test
    public void initFields2() throws Exception {
        PropertiesUtil pu = PropertiesUtil.createPropertyInstance("bean.properties");
        System.out.println("properties:" + JsonUtil.objectToJson(pu));
        Bean2 bean = new Bean2();
        System.out.println(bean);
        BeanUtil.initFields(bean, pu);
        System.out.println(bean);
    }

    class Bean {
        @Properties(key = "fdk.test.bean.id", value = "-1")
        private Integer id;
        @Properties(key = "fdk.test.bean.id", value = "-1")
        private int id2;
        @Properties(key = "fdk.test.bean.name", value = "f")
        private String name;
        @Properties(key = "fdk.test.bean.age", value = "18")
        private Long age;
        @Properties(key = "fdk.test.bean.age", value = "18")
        private long age2;
        @Properties(key = "fdk.test.bean.money", value = "100.01")
        private Double money;
        @Properties(key = "fdk.test.bean.money", value = "100.01")
        private double money2;
        @Properties(key = "fdk.test.bean.man", value = "true")
        private Boolean man;
        @Properties(key = "fdk.test.bean.man", value = "false")
        private boolean man2;

        public Bean setId(Integer id) {
            this.id = id;
            return this;
        }

        public Bean setId2(int id2) {
            this.id2 = id2;
            return this;
        }

        public Bean setName(String name) {
            this.name = name;
            return this;
        }

        public Bean setAge(Long age) {
            this.age = age;
            return this;
        }

        public Bean setAge2(long age2) {
            this.age2 = age2;
            return this;
        }

        public Bean setMoney(Double money) {
            this.money = money;
            return this;
        }

        public Bean setMoney2(double money2) {
            this.money2 = money2;
            return this;
        }

        public Bean setMan(Boolean man) {
            this.man = man;
            return this;
        }

        public Bean setMan2(boolean man2) {
            this.man2 = man2;
            return this;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "id=" + id +
                    ", id2=" + id2 +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", age2=" + age2 +
                    ", money=" + money +
                    ", money2=" + money2 +
                    ", man=" + man +
                    ", man2=" + man2 +
                    '}';
        }
    }
    class Bean2 {
        @Properties(key = "fdk1.test.bean.id")
        private Integer id;
        @Properties(key = "fdk1.test.bean.id")
        private int id2;
        @Properties(key = "fdk1.test.bean.name")
        private String name;
        @Properties(key = "fdk1.test.bean.age")
        private Long age;
        @Properties(key = "fdk1.test.bean.age")
        private long age2;
        @Properties(key = "fdk1.test.bean.money")
        private Double money;
        @Properties(key = "fdk1.test.bean.money")
        private double money2;
        @Properties(key = "fdk1.test.bean.man")
        private Boolean man;
        @Properties(key = "fdk1.test.bean.man")
        private boolean man2;

        public Bean2 setId(Integer id) {
            this.id = id;
            return this;
        }

        public Bean2 setId2(int id2) {
            this.id2 = id2;
            return this;
        }

        public Bean2 setName(String name) {
            this.name = name;
            return this;
        }

        public Bean2 setAge(Long age) {
            this.age = age;
            return this;
        }

        public Bean2 setAge2(long age2) {
            this.age2 = age2;
            return this;
        }

        public Bean2 setMoney(Double money) {
            this.money = money;
            return this;
        }

        public Bean2 setMoney2(double money2) {
            this.money2 = money2;
            return this;
        }

        public Bean2 setMan(Boolean man) {
            this.man = man;
            return this;
        }

        public Bean2 setMan2(boolean man2) {
            this.man2 = man2;
            return this;
        }

        @Override
        public String toString() {
            return "Bean2{" +
                    "id=" + id +
                    ", id2=" + id2 +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", age2=" + age2 +
                    ", money=" + money +
                    ", money2=" + money2 +
                    ", man=" + man +
                    ", man2=" + man2 +
                    '}';
        }
    }
}