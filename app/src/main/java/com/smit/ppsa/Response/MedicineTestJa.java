package com.smit.ppsa.Response;

public class MedicineTestJa {
    String medicineName;
    String medicineDescription;
    Boolean checked;

    public MedicineTestJa(String medicineName, String medicineDescription, Boolean checked) {
        this.medicineName = medicineName;
        this.medicineDescription = medicineDescription;
        this.checked = checked;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineDescription() {
        return medicineDescription;
    }

    public void setMedicineDescription(String medicineDescription) {
        this.medicineDescription = medicineDescription;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
