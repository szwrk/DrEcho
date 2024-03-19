package net.wilamowski.drecho.shared.codegenerator;

import net.wilamowski.drecho.client.application.infra.util.codegenerator.JavaFXPropertyGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class JavaFXPropertyGeneratorTestReader {

    @Test
    void extractNameFromBundle() {
        List<String> passed = Arrays.asList( "#dimen_lv_systole=wymiary_lk_skurcz" ,
                "dimen_lv_diastole=wymiary_lk_rozkurcz" ,
                "dimen_la=wymiary_lp" );
        JavaFXPropertyGenerator generatorPrope = new JavaFXPropertyGenerator();
        String                  result         = generatorPrope.extractNameFromBundle( passed );

        String expected ="dimen_lv_diastole\n" +
                        "dimen_la";
        Assert.assertEquals( expected,result );
    }

    @Test
    void extractNameFromBundleAndAddStringBinding() {
        List<String> passed = Arrays.asList( "#dimen_lv_systole=wymiary_lk_skurcz" ,
                "dimen_lv_diastole=wymiary_lk_rozkurcz" ,
                "dimen_la=wymiary_lp" );
        JavaFXPropertyGenerator generatorPrope = new JavaFXPropertyGenerator();
        String                  result         = generatorPrope.extractNameFromBundleAndAddStringBinding( passed );


        String expected ="dimen_lv_diastoleTextField.textProperty().bindBidirectional(viewModel.dimen_lv_diastoleProperty();\n" +
                "dimen_laTextField.textProperty().bindBidirectional(viewModel.dimen_laProperty();";
        Assert.assertEquals( expected,result );
    }


    @Test
    void extractNameFromBundleAndAddNumberBinding() {
        List<String> passed = Arrays.asList( "#dimen_lv_systole=wymiary_lk_skurcz",
                "echo-tte.mv_regur_wave_area=mitralna pole fali zwrotnej",
                "echo-tte.mv_regur_wave_txt=mitralna fala zwrotna");
        JavaFXPropertyGenerator generatorPrope = new JavaFXPropertyGenerator();
        String                  result         = generatorPrope.extractNameFromBundleAndAutoAddBinding(passed);

        String expected = "mv_regur_wave_areaTextField.textProperty().bindBidirectional(viewModel.mv_regur_wave_areaDoubleProperty(),new DoubleStringConverter());\n" +
        "mv_regur_wave_txtTextField.textProperty().bindBidirectional(viewModel.mv_regur_wave_txtTextProperty());";
        Assert.assertEquals( expected,result );
    }
    @Test
    void generete() {
        List<String> passed = Arrays.asList(
        /*---------------------PASTE BUNDLES--------------------------------*/
                "dm_name=Wymiary jam serca",
                "dm_left_ventricle=Lewa komora",
                "dm_left_ventricle_systole=Lewa komora skurcz",
                "dm_left_ventricle_diastole=Lewa komora rozkurcz",
                "dm_left_atrium=Lewy przedsionek",
                "dm_left_atrium_width=Lewy przedsionek szerokość",
                "dm_left_atrium_length=Lewy przedsionek długość",
                "dm_aorta=Aorta",
                "dm_right_ventricle=Prawa komora",
                "dm_right_ventricle_systole=Prawa komora roz",
                "dm_right_ventricle_diastole=Prawa komora skurcz",
                "dm_right_atrium=Prawy prz edsionek",
                "dm_right_atrium_width=Prawy przedsionek szerokość",
                "dm_right_atrium_length=Prawy przedsionek długość",
                "dm_pulmonary_trunk=Pień płucny",
                "dm_iv_septum_IVS=Mięsień IVS",
                "dm_iv_septum_diastole=Mięsień IVS rozkurcz",
                "dm_iv_septum_systole=Mięsień IVS skurcz",
                "dm_posterior_wall=Tył serca",
                "dm_posterior_wall_diastole=Tył serca rozkurcz",
                "dm_posterior_wall_systole=Tył serca skurcz",
                "dm_mass_LVM=Masa mięsnia LVM",
                "dm_lvmi=LVMI",
                "dm_relative_wall_thickness=RWT",
                "dm_note_txt=Uwagi"
        /*-----------------------------------------------------*/
        );
        JavaFXPropertyGenerator bindings       = new JavaFXPropertyGenerator();
        String                  bindingsresult = bindings.extractNameFromBundleAndAutoAddBinding(passed);
        System.out.println(bindingsresult);
        System.out.println("###########################################################################");
        JavaFXPropertyGenerator properties       = new JavaFXPropertyGenerator();
        String                  propertiesresult = properties.extractNameFromBundleAndCreateProperties(passed);
        System.out.println(propertiesresult);
    }

}