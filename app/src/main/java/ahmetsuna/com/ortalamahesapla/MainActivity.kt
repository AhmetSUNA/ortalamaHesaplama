package ahmetsuna.com.ortalamahesapla

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.yeni_ders_layout.view.*

class MainActivity : AppCompatActivity() {

    private val DERSLER = arrayOf("BOM", "Sayısalİşaretİşleme", "İşletimSistemleri", "NesneyeDayalıProgramlama", "BMG", "HesaplamaKuramı")
    private var tumDerslerinBilgileri: ArrayList<Dersler> = ArrayList(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, DERSLER)
        etDersAd.setAdapter(adapter)

        btnDersEkle.setOnClickListener {

            if (!etDersAd.text.isNullOrEmpty()) {
                var inflater = LayoutInflater.from(this)
                /*var inflater2 = layoutInflater üstteki ifadeyle aynı
                var inflater3 = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                inflater3.inflate() */

                //yeniDersLayout'u xml'den java'ya dönüştürüyoruz
                var yeniDersView = inflater.inflate(R.layout.yeni_ders_layout, null)

                yeniDersView.etYeniDersAd.setAdapter(adapter)

                //statik alandan kullanıcının girdigi değerleri alalım
                var dersAdi = etDersAd.text.toString()
                var dersKredi = spnDersKredi.selectedItemPosition
                var dersHarfi = spnDersNotu.selectedItemPosition

                //dinamik oluşturulacak layoutta bulunan view öğelerine bu değerleri atayalım
                //böylece yeni latoutumuz kullanıcının girmiş olduğu değerler ile oluşturulacaktır.
                yeniDersView.etYeniDersAd.setText(dersAdi)
                yeniDersView.spnYeniDersKredi.setSelection(dersKredi)
                yeniDersView.spnYeniDersNotu.setSelection(dersHarfi)

                //sil butonuna silme görevi atandı
                yeniDersView.btnDersSil.setOnClickListener {

                    rootLayout.removeView(yeniDersView)
                    //eğer dersler listesi bos ise hesapla butonu saklanır,değilse görünür yapılır
                    if (rootLayout.childCount == 0) {
                        btnOrtHesapla.visibility = View.INVISIBLE
                    } else btnOrtHesapla.visibility = View.VISIBLE
                }

                rootLayout.addView(yeniDersView)
                //Yeni bir ders alanı eklenirse hesapla butonu görünür yapıldı
                btnOrtHesapla.visibility = View.VISIBLE

                sifirla()

            }
            //Eğer ders adı girilmeden ekle butonu tıklanırsa uyarı verir
            else {
                FancyToast.makeText(this, "Ders Adını Giriniz !", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show()

            }
        }
    }

    //Yeni bir ders eklendikten sonra en baştaki layoutta bulunan alanların değerleri temizlendi
    fun sifirla() {
        etDersAd.setText("")
        spnDersKredi.setSelection(0)
        spnDersNotu.setSelection(0)
    }

    fun ortalamaHesapla(view: View) {

        var toplamNot = 0.0
        var toplamKredi = 0.0

        for (i in 0..rootLayout.childCount - 1) {

            var tekSatir = rootLayout.getChildAt(i)

            var geciciDers = Dersler(tekSatir.etYeniDersAd.text.toString(),
                    ((tekSatir.spnYeniDersKredi.selectedItemPosition) + 1).toString(),
                    tekSatir.spnYeniDersNotu.selectedItem.toString())

            tumDerslerinBilgileri.add(geciciDers)
        }

        for (oankiDers in tumDerslerinBilgileri) {

            toplamNot += harfiNotaÇevir(oankiDers.dersHarfNotu) * (oankiDers.dersKredi.toDouble())
            toplamKredi += oankiDers.dersKredi.toDouble()
        }
        //ORTALAMA HESABI YAPILIR VE KULLANICIYA GÖSTERİLİR
        FancyToast.makeText(this, "ORTALAMA: " + (toplamNot / toplamKredi).formatla(2), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show()
        tumDerslerinBilgileri.clear()
    }

    fun harfiNotaÇevir(gelenNotHarfDegeri: String): Double {

        var deger = 0.0

        when (gelenNotHarfDegeri) {

            "AA" -> deger = 4.0
            "BA" -> deger = 3.5
            "BB" -> deger = 3.0
            "CB" -> deger = 2.5
            "CC" -> deger = 2.0
            "DC" -> deger = 1.5
            "DD" -> deger = 1.0
            "FF" -> deger = 0.0
        }

        return deger
    }
    fun Double.formatla(kacTaneRakam:Int) = java.lang.String.format("%.${kacTaneRakam}f",this)
}
