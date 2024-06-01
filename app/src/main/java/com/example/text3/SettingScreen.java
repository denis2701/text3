package com.example.text3;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView statsNameTextView = findViewById(R.id.StatsName);
        TextView tipsTextView = findViewById(R.id.textCategories3);

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String deviceName;
        if (model.startsWith(manufacturer)) {
            deviceName = capitalize(model);
        } else {
            deviceName = capitalize(manufacturer) + " " + model;
        }

        statsNameTextView.setText(deviceName);

        // Set the tips based on the device brand
        String tips = getOptimizationTips(manufacturer);
        tipsTextView.setText(tips);
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private String getOptimizationTips(String manufacturer) {
        switch (manufacturer.toLowerCase()) {
                    case "samsung":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Akumuliatoriaus optimizavimas: Eikite į Nustatymai > Programos > Specialus Priėjimas > Optimizuoti Akumuliatoriaus Naudojimą. Išjunkite optimizavimą jūsų programėlei.\n\n" +
                                "Įrenginio priežiūra: Eikite į Nustatymai > Įrenginio Priežiūra > Akumuliatorius. Pridėkite savo programėlę į 'Programėlės, kurios nebus užmigdytos' sąrašą.\n\n" +
                                "Programėlės energijos valdymas: Eikite į Nustatymai > Įrenginio Priežiūra > Akumuliatorius > Programėlės Energijos Valdymas. Išjunkite 'Neaktyvios programėlės užmigdomos' arba pridėkite savo programėlę į išimčių sąrašą.";
                    case "huawei":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Apsaugotos programėlės: Eikite į Nustatymai > Akumuliatorius > Programėlės Paleidimas. Raskite savo programėlę ir pasirinkite 'Valdyti rankiniu būdu', tada įjunkite 'Automatinis paleidimas', 'Antrinis paleidimas' ir 'Vykdyti fone'.\n\n" +
                                "Akumuliatoriaus optimizavimas: Eikite į Nustatymai > Akumuliatorius > Akumuliatoriaus Optimizavimas. Raskite savo programėlę ir pasirinkite 'Neleisti' dėl akumuliatoriaus optimizavimo.";
                    case "xiaomi":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Automatinis paleidimas: Eikite į Nustatymai > Leidimai > Automatinis Paleidimas. Įjunkite automatinį paleidimą jūsų programėlei.\n\n" +
                                "Akumuliatoriaus taupytojas: Eikite į Nustatymai > Akumuliatorius ir Veikimas > Akumuliatoriaus Taupytojas. Pasirinkite 'Pasirinkti programėles' ir nustatykite savo programėlę 'Be apribojimų'.\nv" +
                                "Programėlės užraktas: Eikite į Nustatymai > Programėlės > Programėlės Užraktas. Įsitikinkite, kad jūsų programėlė nėra užrakinta.";
                    case "oppo":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Programėlių valdymas: Eikite į Nustatymai > Akumuliatorius > Energijos Naudojimas. Raskite savo programėlę ir pasirinkite 'Leisti fono veikimą'.\n\n" +
                                "Akumuliatoriaus optimizavimas: Eikite į Nustatymai > Akumuliatorius > Akumuliatoriaus Optimizavimas. Raskite savo programėlę ir pasirinkite 'Neoptimizuoti'.";
                    case "vivo":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Fono valdymas: Eikite į Nustatymai > Akumuliatorius > Didelė Fono Energijos Sąnauda. Leiskite didelę fono energijos sąnaudą jūsų programėlei.\n\n" +
                                "Akumuliatoriaus optimizavimas: Eikite į Nustatymai > Akumuliatorius > Akumuliatoriaus Taupytojas. Išskirkite savo programėlę iš akumuliatoriaus optimizavimo.";

                    case "oneplus":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Akumuliatoriaus optimizavimas: Eikite į Nustatymai > Akumuliatorius > Akumuliatoriaus Optimizavimas. Raskite savo programėlę ir pasirinkite 'Neoptimizuoti'.\n\n" +
                                "Išplėstinis optimizavimas: Eikite į Nustatymai > Akumuliatorius > Išplėstinis Optimizavimas. Išjunkite 'Miego būsena optimizavimas'.";

                    case "lg":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Akumuliatoriaus taupytojas: Eikite į Nustatymai > Akumuliatorius ir Energijos Taupymas > Akumuliatoriaus Naudojimas. Išjunkite akumuliatoriaus optimizavimą jūsų programėlei.\n\n" +
                                "Programėlės optimizavimas: Eikite į Nustatymai > Bendrieji > Akumuliatorius ir Energijos Taupymas > Akumuliatoriaus Naudojimas. Pasirinkite 'Nepaisyti optimizavimo' jūsų programėlei.";

                    case "sony":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Akumuliatoriaus optimizavimas: Eikite į Nustatymai > Akumuliatorius > Akumuliatoriaus Optimizavimas. Pasirinkite 'Programėlės' ir tada 'Visos programėlės'. Raskite savo programėlę ir pasirinkite 'Neoptimizuoti'.";

                    case "google":
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Akumuliatoriaus optimizavimas: Užtikrinkite, kad 'Akumuliatoriaus optimizavimas' būtų išjungtas jūsų programėlei. Eikite į Nustatymai > Baterija > Daugiau > Baterijos optimizavimas.";

                    default:
                        return "Priekinė paslauga: Įgyvendinkite savo programėlę kaip priekinę paslaugą, kad padidintumėte jos prioritetą ir sumažintumėte tikimybę, kad sistema ją uždarys.\n\n" +
                                "Lipnios pranešimai: Naudokite lipnius pranešimus, kad jie būtų mažiau tikėtina automatiškai pašalinami sistemos.\n\n" +
                                "Akumuliatoriaus optimizavimo išimtys: Paskatinkite vartotojus išskirti jūsų programėlę iš akumuliatoriaus optimizavimo nustatymų.\n\n" +
                                "JobScheduler: Naudokite JobScheduler, kad atliktumėte periodines užduotis, užtikrindami, kad jūsų programėlė liktų aktyvi ir stebima.\n\n" +
                                "Specifiniai patarimai pagal gamintoją nėra pateikti jūsų įrenginiui. Įsitikinkite, kad patikrinote akumuliatoriaus ir kitus energijos valdymo nustatymus.";
                }
    }
}