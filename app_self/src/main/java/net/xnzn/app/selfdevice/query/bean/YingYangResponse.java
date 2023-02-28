package net.xnzn.app.selfdevice.query.bean;

public class YingYangResponse {

    /**
     * 年龄	integer(int32)
     * calories	热量实际摄入	number
     * caloriesIndex	热量指数	number
     * carbohydrate	碳水化合物实际摄入	number
     * carbohydrateIndex	碳水化合物指数	number
     * cholesterol	胆固醇实际摄入	number
     * cholesterolIndex	胆固醇指数	number
     * custName	人员姓名	string
     * dietaryFiber	膳食纤维实际摄入	number
     * dietaryFiberIndex	膳食纤维指数	number
     * fat	脂肪实际摄入	number
     * fatIndex	脂肪指数	number
     * orgFullName	部门	string
     * protein	蛋白质实际摄入	number
     * proteinIndex	蛋白质指数	number
     * referenceCalories	热量推荐摄入	number
     * referenceCarbohydrate	碳水化合物推荐摄入	number
     * referenceCholesterol	胆固醇推荐摄入	number
     * referenceDietaryFiber	膳食纤维推荐摄入	number
     * referenceFat	脂肪推荐摄入	number
     * referenceProtein	蛋白质推荐摄入
     */

    private int age;
    private String custName;
    private String orgFullName;
    private float calories;
    private float caloriesIndex;
    private float carbohydrate;
    private float carbohydrateIndex;
    private float cholesterol;
    private float cholesterolIndex;
    private float dietaryFiber;
    private float dietaryFiberIndex;
    private float fat;
    private float fatIndex;
    private float protein;
    private float proteinIndex;
    private float referenceCalories;
    private float referenceCarbohydrate;
    private float referenceCholesterol;
    private float referenceDietaryFiber;
    private float referenceFat;
    private float referenceProtein;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getCaloriesIndex() {
        return caloriesIndex;
    }

    public void setCaloriesIndex(float caloriesIndex) {
        this.caloriesIndex = caloriesIndex;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public float getCarbohydrateIndex() {
        return carbohydrateIndex;
    }

    public void setCarbohydrateIndex(float carbohydrateIndex) {
        this.carbohydrateIndex = carbohydrateIndex;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getCholesterolIndex() {
        return cholesterolIndex;
    }

    public void setCholesterolIndex(float cholesterolIndex) {
        this.cholesterolIndex = cholesterolIndex;
    }

    public float getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(float dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public float getDietaryFiberIndex() {
        return dietaryFiberIndex;
    }

    public void setDietaryFiberIndex(float dietaryFiberIndex) {
        this.dietaryFiberIndex = dietaryFiberIndex;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getFatIndex() {
        return fatIndex;
    }

    public void setFatIndex(float fatIndex) {
        this.fatIndex = fatIndex;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getProteinIndex() {
        return proteinIndex;
    }

    public void setProteinIndex(float proteinIndex) {
        this.proteinIndex = proteinIndex;
    }

    public float getReferenceCalories() {
        return referenceCalories;
    }

    public void setReferenceCalories(float referenceCalories) {
        this.referenceCalories = referenceCalories;
    }

    public float getReferenceCarbohydrate() {
        return referenceCarbohydrate;
    }

    public void setReferenceCarbohydrate(float referenceCarbohydrate) {
        this.referenceCarbohydrate = referenceCarbohydrate;
    }

    public float getReferenceCholesterol() {
        return referenceCholesterol;
    }

    public void setReferenceCholesterol(float referenceCholesterol) {
        this.referenceCholesterol = referenceCholesterol;
    }

    public float getReferenceDietaryFiber() {
        return referenceDietaryFiber;
    }

    public void setReferenceDietaryFiber(float referenceDietaryFiber) {
        this.referenceDietaryFiber = referenceDietaryFiber;
    }

    public float getReferenceFat() {
        return referenceFat;
    }

    public void setReferenceFat(float referenceFat) {
        this.referenceFat = referenceFat;
    }

    public float getReferenceProtein() {
        return referenceProtein;
    }

    public void setReferenceProtein(float referenceProtein) {
        this.referenceProtein = referenceProtein;
    }
}
