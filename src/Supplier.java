import java.io.Serializable;

    public class Supplier implements Serializable {
        private String supplierName;

        public Supplier(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getName() {
            return supplierName;
        }
    }
