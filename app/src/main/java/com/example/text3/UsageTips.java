package com.example.text3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsageTips extends AppCompatActivity {

    private String[] allTips = {
            "Nustatykite konkrečias ribas: Nustatykite dienos ar savaitės laiko limitą telefono naudojimui ir laikykitės jo.",
            "Naudokite ekrano laiko stebėjimo programėles: Programėlės kaip „Moment“ ar „Screen Time“ gali padėti jums stebėti ir valdyti telefono naudojimą.",
            "Išjunkite nereikalingus pranešimus: Išjunkite pranešimus programėlėms, kurios nėra būtinos, kad sumažintumėte dėmesio išblaškymą.",
            "Sukurkite zonas be telefono: Pažymėkite tam tikras vietas, tokias kaip miegamasis ar valgomasis stalas, kaip zonas be telefono, kad skatintumėte bendravimą be interneto.",
            "Nustatykite laikus be telefono: Skirkite laiko dienos metu, kai nenaudosite telefono, pavyzdžiui, valgant ar prieš miegą.",
            "Prioritetą teikite realiam bendravimui: Koncentruokitės į bendravimą su žmonėmis akis į akį, o ne per telefoną.",
            "Praktikuokite sąmoningumą: Stebėkite savo telefono naudojimo įpročius ir sąmoningai stenkitės mažinti nereikalingą ekrano laiką.",
            "Nustatykite tikslus veikloms be telefono: Skirkite laiko pomėgiams, sportui, skaitymui ar kitoms veikloms, kurios nereikalauja telefono.",
            "Naudokite fizinį žadintuvą: Vietoj telefono kaip žadintuvo, rinkitės tradicinį žadintuvą, kad išvengtumėte telefono tikrinimo pirmiausia ryte.",
            "Laikykite telefoną nematomoje vietoje: Laikykite telefoną kitame kambaryje ar nepasiekiamoje vietoje, kai dirbate ar leidžiate laiką su kitais.",
            "Įjunkite pilkos spalvos režimą: Perjunkite telefoną į pilkos spalvos režimą, kad jis būtų mažiau patrauklus ir sumažintų laiką, praleidžiamą su juo.",
            "Išjunkite automatinį vaizdo įrašų paleidimą: Užkirskite kelią vaizdo įrašų automatinio paleidimo socialinių tinklų programėlėse, kad sumažintumėte pagundą.",
            "Naudokite lėktuvo režimą: Įjunkite lėktuvo režimą, kai reikia sutelkti dėmesį į užduotis ar praleidžiate laiką su kitais.",
            "Planuokite pertraukas telefonui: Skirkite trumpas pertraukas dienos metu telefonui patikrinti, vietoj nuolatinio daugiaveikimo.",
            "Ribokite socialinių tinklų naudojimą: Nustatykite laiko limitą socialinių tinklų programėlėms ir apsvarstykite jų išdiegimą ar išjungimą, jei jos užima per daug jūsų laiko.",
            "Praktikuokite 20-20-20 taisyklę: Kas 20 minučių darykite 20 sekundžių pertrauką, kad pažvelgtumėte į ką nors 20 pėdų atstumu, kad sumažintumėte akių įtampą ir telefono naudojimą.",
            "Išjunkite pirkimus programėlėse: Išjunkite galimybę pirkti programėlėse, kad išvengtumėte impulsyvių pirkimų ir pernelyg daug laiko praleidžiamą žaidimams.",
            "Naudokite minimalistinį pagrindinį ekraną: Pašalinkite nereikalingas programėles ir valdiklius iš pagrindinio ekrano, kad sumažintumėte vizualinę netvarką ir pagundas.",
            "Nustatykite programėlių naudojimo priminimus: Naudokite įmontuotas funkcijas ar trečiųjų šalių programėles, kad gautumėte priminimus, kai pasieksite nustatytus programėlių naudojimo limitus.",
            "Išjunkite el. pašto pranešimus: Išjunkite el. pašto pranešimus, kad sumažintumėte norą nuolat tikrinti savo pašto dėžutę.",
            "Veskite telefono naudojimo dienoraštį: Sekite savo telefono naudojimo įpročius, kad nustatytumėte modelius ir tobulintinas sritis.",
            "Planuokite veiklas be interneto: Planuokite išvykas, treniruotes ar kūrybinius užsiėmimus, kuriems nereikia telefono.",
            "Naudokite telefono stovą: Paremkite telefoną ant stovo, kai jį naudojate, kad sumažintumėte nesąmoningą naršymą ir skatintumėte tyčinį naudojimą.",
            "Praktikuokite skaitmeninio detokso dienas: Skirkite dienas, kai visiškai atsijungiate nuo telefono ir koncentruojatės į veiklas be interneto.",
            "Deleguokite užduotis: Deleguokite užduotis, kurioms reikia telefono naudojimo, kitiems žmonėms, jei tai įmanoma, pvz., tikrinant el. laiškus ar skambinant.",
            "Įjunkite režimą „Netrukdyti“: Naudokite režimą „Netrukdyti“, kad nutildytumėte pranešimus per susitikimus, pokalbius ar ramybės momentus.",
            "Nustatykite ribas su kitais: Praneškite draugams ir šeimos nariams apie savo norą sumažinti telefono naudojimą ir paprašykite jų palaikymo.",
            "Išjunkite vibracijos pranešimus: Išjunkite vibracijos pranešimus, kad sumažintumėte dėmesio išblaškymą ir priklausomybę nuo telefono pranešimų.",
            "Naudokite atskirą kamerą: Nešiokite atskirą kamerą, kad fotografuotumėte, vietoj to, kad naudotumėte telefoną akimirkų fiksavimui.",
            "Ribokite programėlių leidimus: Peržiūrėkite ir ribokite programėlėms suteiktus leidimus, kad sumažintumėte duomenų rinkimą ir galimus dėmesio išblaškymus.",
            "Praktikuokite atidėliotą pasitenkinimą: Atidėkite telefono tikrinimą gavus pranešimus, kad atsikratytumėte įpročio nuolat reaguoti į pranešimus.",
            "Atsisakykite prenumeratų el. laiškų sąrašuose: Pašalinkite save iš el. laiškų sąrašų, kurie siunčia dažnus atnaujinimus ar reklamas, kad sumažintumėte pašto dėžutės netvarką ir pagundas.",
            "Nustatykite ribas su darbu: Aiškiai atskirkite darbo ir asmeninį laiką, kad darbo su telefonu naudojimas neperžengtų laisvalaikio laiko.",
            "Naudokite slaptažodžių tvarkyklę: Naudokite slaptažodžių tvarkyklę, kad supaprastintumėte prisijungimus ir sumažintumėte laiką, praleidžiamą įvedant slaptažodžius telefone.",
            "Planuokite maitinimą be įrenginių: Mėgaukitės maistu be telefono, kad visiškai įsitrauktumėte į valgymo patirtį ir bendrautumėte su kitais.",
            "Praktikuokite aktyvų klausymą: Koncentruokitės į aktyvų kitų klausymą pokalbių metu, vietoj to, kad daugiafunkciškai naudotumėte telefoną.",
            "Investuokite į el. knygų skaitytuvą: Naudokite el. knygų skaitytuvą skaitant skaitmenines knygas, vietoj telefono, kad sumažintumėte dėmesio išblaškymus ir akių įtampą.",
            "Prioritetą teikite būtinoms programėlėms: Laikykite tik būtinas programėles savo telefone ir pašalinkite ar paslėpkite nereikalingas, kad sumažintumėte pagundas.",
            "Naudokite tėvų kontrolę: Jei taikoma, naudokite tėvų kontrolės funkcijas, kad apribotumėte telefono naudojimą sau ar šeimos nariams.",
            "Nustatykite miego laiką telefonui: Nustatykite konkretų laiką vakare, kai nustosite naudotis telefonu, kad pagerintumėte miego higieną.",
            "Praktikuokite vieno naudojimo taisyklę: Naudokite tik vieną programėlę arba tikrinkite vieną socialinę platformą vienu metu, kad išvengtumėte nesibaigiančio naršymo.",
            "Suraskite alternatyvių pramogų šaltinių: Atraskite pomėgius, veiklas lauke ar kūrybinius užsiėmimus, kurie teikia malonumą be telefono.",
            "Pasirinkite treniruotes be telefono: Palikite telefoną namuose arba naudokite atskirą muzikos grotuvą treniruotėms, kad sumažintumėte dėmesio išblaškymą.",
            "Išjunkite automatinį paleidimą srautinio perdavimo paslaugose: Išjunkite automatinio paleidimo funkcijas srautinio perdavimo platformose, kad išvengtumėte peržiūros sesijų.",
            "Naudokite kelionės laiką be telefono: Naudokite kelionės laiką kaip galimybę atsijungti nuo telefono ir mėgautis aplinka ar bendrauti.",
            "Sukurkite įkrovimo stotį: Pažymėkite konkrečią vietą savo namuose telefono įkrovimui, toliau nuo pagrindinių gyvenamųjų erdvių, kad sumažintumėte pagundas.",
            "Praktikuokite savidiscipliną: Priminkite sau, kodėl norite sumažinti telefono naudojimą ir laikykitės savo tikslų.",
            "Kreipkitės į kitus dėl palaikymo: Prisijunkite prie internetinių bendruomenių ar atsakomybės grupių, skirtų telefono naudojimo mažinimui, kad gautumėte abipusį palaikymą ir paskatinimą.",
            "Apdovanokite save: Švęskite pasiekimus mažinant telefono naudojimą apdovanojimais, kurie sustiprina teigiamą elgesį.",
            "Būkite kantrūs su savimi: Įpročių keitimas reikalauja laiko ir pastangų, todėl būkite kantrūs ir malonūs sau, dirbdami link telefono naudojimo mažinimo."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usagetips);

        TextView tip1TextView = findViewById(R.id.tiptext);
        TextView tip2TextView = findViewById(R.id.tiptext2);
        TextView tip3TextView = findViewById(R.id.tiptext3);

        List<String> selectedTips = getRandomTips(3);

        if (selectedTips.size() >= 1) {
            tip1TextView.setText(selectedTips.get(0));
        }
        if (selectedTips.size() >= 2) {
            tip2TextView.setText(selectedTips.get(1));
        }
        if (selectedTips.size() >= 3) {
            tip3TextView.setText(selectedTips.get(2));
        }
    }

    private List<String> getRandomTips(int count) {
        List<String> randomTips = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(allTips.length);
            randomTips.add(allTips[randomIndex]);
        }

        return randomTips;
    }
}