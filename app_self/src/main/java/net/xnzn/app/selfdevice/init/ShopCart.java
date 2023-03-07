package net.xnzn.app.selfdevice.init;

import net.xnzn.leniu_http.yunshitang.pay.common.CommonProduct;
import net.xnzn.lib_commin_ui.utils.ArithHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShopCart {


    private static ShopCart instance = new ShopCart();

    public static ShopCart getInstance() {
        return instance;
    }

    private int totalAmount;
    private int totalNum;
    private String tuoPlateKxh;


    public void setTuoPlateKxh(String tuoPlateKxh) {
        this.tuoPlateKxh = tuoPlateKxh;
    }

    public String getTuoPlateKxh() {
        return tuoPlateKxh;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public boolean isAllCrcError() {
        int count = 0;
        int crcErrorCount = 0;
        for (int i = 0; i < products.size(); i++) {
            CommonProduct item = products.get(i);
            count++;
            if (item.isPlate()) {
                if (!item.isCrcSuccess()) {
                    crcErrorCount++;
                }
            }
        }
        if (count == crcErrorCount && count != 0) {
            return true;
        }
        return false;
    }


    public void updateTotalAmount() {
        int size = getProducts().size();
        if (getProducts().size() == 0) {
            totalAmount = 0;
            totalNum = 0;
            return;
        }

        int total = 0;
        int num = 0;
        for (int i = 0; i < size; i++) {
            CommonProduct item = getProducts().get(i);
            if (item.isPlate() && item.isPaied()) {
                continue;
            }

            if (item.getSalesMode() == 1) {
                if (item.getSalesPrice() == null) {

                } else {
                    total = (int) ArithHelper.add(total, ArithHelper.mul(item.getSalesPrice(), item.getProductNum()));
                }
                num = (int) ArithHelper.add(num, item.getProductNum());
            } else {
                total = (int) ArithHelper.add(total, ArithHelper.div(ArithHelper.mul(item.getSalesPrice(), item.getProductNum()), item.getWeight()));
                num = (int) ArithHelper.add(num, 1);
            }
        }
        totalNum = num;
        totalAmount = total;
    }


    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    private List<CommonProduct> products = new ArrayList<>();


    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<CommonProduct> getProducts() {
        return products;
    }

    public boolean isAllPaied() {
        for (int i = 0; i < products.size(); i++) {
            CommonProduct product = products.get(i);
            if ((product.isPlate() && !product.isPaied()) || product.isSelf() || product.isKeyboard() || product.isNormal()) {
                return false;
            }
        }
        return true;
    }


    public boolean hasErrorPlate() {
        for (int i = 0; i < products.size(); i++) {
            CommonProduct product = products.get(i);
            if (product.isPlate()) {
                if (!product.isCrcSuccess()) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setProducts(List<CommonProduct> products) {
        this.products = products;
    }


    public void clear() {
        totalAmount = 0;
        totalNum = 0;
        tuoPlateKxh = null;
        products.clear();
    }

    public void reduceProduct(CommonProduct product) {
        if (product.getProductNum() == 1) {
            products.remove(product);
            return;
        }
        product.setProductNum(product.getProductNum() - 1);
    }

    public void addProduct(CommonProduct product) {
        if (product.getProductType() == 2) {
            //按键金额
            product.setProductNum(1);
            getProducts().add(0, product);
            return;
        }

        CommonProduct oldProduct = getProduct(product);
        if (oldProduct == null) {
            CommonProduct cloneProduct = product.clone();
            cloneProduct.setProductNum(1);
            getProducts().add(0, cloneProduct);
            return;
        }
        oldProduct.setProductNum(oldProduct.getProductNum() + 1);
    }


    private CommonProduct getProduct(CommonProduct product) {
        Iterator iterator = products.listIterator();
        while (iterator.hasNext()) {
            CommonProduct item = (CommonProduct) iterator.next();
            if (item == product) {
                return item;
            }
        }
        return null;
    }


    public List<String> getNrvData() {
        StringBuilder builder = new StringBuilder();
        if (getProducts() == null) {
            return null;
        }
        int size = getProducts().size();
        //热量
        double freliang = 0;
        //蛋白质
        double fdanbaizhi = 0;
        //脂肪
        double fzhifang = 0;
        //碳水化合物
        double ftanshui = 0;
        //纤维
        double fqianwei = 0;
        //胆固醇
        double fdanguchun = 0;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CommonProduct food = getProducts().get(i);
            if (food.getCalories() != null)
                freliang = ArithHelper.add(freliang, ArithHelper.mul(food.getCalories() + "", food.getProductNum() + ""));
            if (food.getProtein() != null)
                fdanbaizhi = ArithHelper.add(fdanbaizhi, ArithHelper.mul(food.getProtein() + "", food.getProductNum() + ""));
            if (food.getFat() != null)
                fzhifang = ArithHelper.add(fzhifang, ArithHelper.mul(food.getFat() + "", food.getProductNum() + ""));
            if (food.getCarbohydrate() != null)
                ftanshui = ArithHelper.add(ftanshui, ArithHelper.mul(food.getCarbohydrate() + "", food.getProductNum() + ""));
            if (food.getDietaryFiber() != null)
                fqianwei = ArithHelper.add(fqianwei, ArithHelper.mul(food.getDietaryFiber() + "", food.getProductNum() + ""));
            if (food.getCholesterol() != null)
                fdanguchun = ArithHelper.add(fdanguchun, ArithHelper.mul(food.getCholesterol() + "", food.getProductNum() + ""));
        }

        list.add(freliang + "");
        list.add(fdanbaizhi + "");
        list.add(fzhifang + "");
        list.add(ftanshui + "");
        list.add(fqianwei + "");
        list.add(fdanguchun + "");

        return list;

    }

}
