package app
import org.w3c.dom.html.HTMLOptGroupElement

import java.nio.charset.Charset
import java.util.Calendar
import scala.reflect.internal.NoPhase.id




object MinimalApplication extends cask.MainRoutes{


  class Tilaus(val order_nro: Int, val customer: String,val food: String,val maksutapa: String){

      override def toString: String = "#"+order_nro.toString +" to "+ customer+": "+ food + " at "+ time_promised +" paid with "+ maksutapa // THIS IS FULL
      def customersSTring: String =" to "+ this.order_nro.toString +" at "+ time_promised  // THIS IS CUSTOMER ONLY

      // TIME ACCESS / laskee luvattua aikaa
      val tilattu = Calendar.getInstance().getTime
    var a = true
      var luvattu_hour = tilattu.getHours
      var luvattu_mins = tilattu.getMinutes
      var time_to_wait = 20 * yourNumber
      var time_promised: String = "00.00"
      def calculate_time_promised: Unit = {
        while( time_to_wait > 0) {
          if ( time_to_wait + luvattu_mins < 60) {
            luvattu_mins += time_to_wait
            time_to_wait = 0
          }
          else if (time_to_wait + luvattu_mins> 60) {
            time_to_wait -= 60 - luvattu_mins
            luvattu_mins = 0
            if (luvattu_hour == 23 ) {luvattu_hour = 0} else {luvattu_hour += 1 }
          }else {}
        }
        if( luvattu_mins.toString.length != 1) {

        time_promised ="Order nro:"+this.order_nro.toString+" ready at: "+ luvattu_hour.toString+"."+luvattu_mins.toString
        }else {time_promised ="Order nro:"+this.order_nro.toString+" ready at: "+ luvattu_hour.toString+"."+"0"+luvattu_mins.toString}


      }
      calculate_time_promised


    }
      // Paikka säilöä tehdyt tilaukset
      var tilaus_vector: Vector[ Tilaus]= Vector[Tilaus]()
      def tilaukset_stringinä = {
        var palautusString = ""
        if (tilaus_vector.length > 0){


        messages = {Vector(
        ("", ""),
        )}
        tilaus_vector.zipWithIndex.foreach{ case (x,i) => {
          // messages = messages :+ ("",tilaus_vector(i).toString)  // admin näkymä
          messages = messages :+ ("",tilaus_vector(i).order_nro.toString+tilaus_vector(i).time_promised.toString)  // customer näkymä
        }}
      }else{""}}

      // yourNumber calculates the number of next order
      def yourNumber: Int = tilaus_vector.length +1
      var messages = {Vector(
        ("", ""),
        ("", ""),
      )}


  def imgBox(source: String) = div(
    img(src:=source),
  )
  def embed = {
    div(
    iframe( width:="560", height:="315", src:="https://www.youtube.com/embed/dQw4w9WgXcQ"),
    )
  }


  def messageList() = {
    meta(charset:="UTF-8")

    var palautusVector: List[String] = List("")
    frag(
      for( tilaus <- tilaus_vector )
      yield p(b(tilaus.toString))

    )
  }
    def messageList_Customer() = {
    meta(charset:="UTF-8")

    var palautusVector: List[String] = List("")
    frag(
      for( tilaus <- tilaus_vector )
      yield p(b(tilaus.customersSTring))

    )
  }


  // postHello aka tilauksen käsittely/ formin lähetys
  @cask.postForm("/")
  def postHello(name: String, msg: String, maksutapa: String, receipt: String) = { // RECEIPT GIVEN, VALUE NOT USED
    if (name == "") hello(Some("Name cannot be empty"), Some(name), Some(msg))
    else if (name.length >= 20) hello(Some("Name cannot be longer than 20 characters"), Some(name), Some(msg))
    else if (msg == "") hello(Some("Message cannot be empty"), Some(name), Some(msg))
    else if (msg.length >= 160) hello(Some("Message cannot be longer than 160 characters"), Some(name), Some(msg))
    else {
      messages = messages :+ (name -> msg)
      this.tilaus_vector = this.tilaus_vector :+ new Tilaus(yourNumber, name, msg, maksutapa)
      hello(None, None, None)

    }
  }

    @cask.postForm("/customer")
  def postCustomer(name: String, msg: String, maksutapa: String, receipt: String) = { // RECEIPT GIVEN, VALUE NOT USED
    if (name == "") customer(Some("Name cannot be empty"), Some(name), Some(msg))
    else if (name.length >= 20) customer(Some("Name cannot be longer than 20 characters"), Some(name), Some(msg))
    else if (msg == "") customer(Some("Message cannot be empty"), Some(name), Some(msg))
    else if (msg.length >= 160) customer(Some("Message cannot be longer than 160 characters"), Some(name), Some(msg))
    else {
      messages = messages :+ (name -> msg)
      this.tilaus_vector = this.tilaus_vector :+ new Tilaus(yourNumber, name, msg, maksutapa)


      customer(None, None, None)

    }
  }
  // "CUSTOMER"


  @cask.get("/admin")
  def customer(errorOpt: Option[String] = None,
                            userName: Option[String] = None,
                            msg: Option[String] = None
                           ) = {
    html(
      head(
        meta(charset:="UTF-8"),
        link(
          rel := "stylesheet",
          href := "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css",

          src := "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Atria_Jauhelihapizza.jpg/1280px-Atria_Jauhelihapizza.jpg"


        ),
          scalatags.Text.tags2.title("Pizza orders n sheet Admin view")

      ),


      body(
        meta(charset:="UTF-8"),
        style := "background-image: url(https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Atria_Jauhelihapizza.jpg/1280px-Atria_Jauhelihapizza.jpg)",


        div(cls := "container")(
          h1("Pizza orders n sheet // Admin view"),
          h2(s"Please enter your order.\nYour order is number #"+yourNumber.toString),
          //h3(tilaukset_stringinä),
          hr,
          div(id := "Orderlist") (
            messageList()
          )),

          hr,


          for(error <- errorOpt)
          yield i(color.red)(error),


          // Tässä tulee se pääasiallinen form
          form(action := "/", method := "post")(
            meta(charset:="UTF-8"),


            input(`type` := "push", placeholder := yourNumber.toString,disabled:="disabled", width := "20%"),
            input(`type` := "text", name := "name",
            placeholder := "Name", width := "10%",
            userName.map( value := _) ),
            input( `type`:= "text", name := "msg", placeholder := "Please write a your order!", width:="40%", // possible to do Tilauksen jonkinlaisena valikkona
            msg.map(value := _)),
            select( id:= "receipt", name:="receipt", // FORM ASKS THIS, DOES NOT CHANGE ANYTHING
                option("No receipt needed", value:= "No receipt needed"),
                option("Receipt does not matter", value:= "Receipt does not matter"),
                option("Receipt wanted, not given", value:= "Receipt wanted, not given"),
                  ),

            select( id:= "maksutapa", name:= "maksutapa",
                option("cash", value:="cash"),
                option("card", value:= "card"),
                option("other", value:="other"),
                  ),
            input(`type` := "submit", width := "10%")
          ),
        a(href:="/", "To Customer page"),

      if (tilaus_vector.exists(_.toString.contains("Rick")) ||tilaus_vector.exists(_.toString.contains("rick"))) {embed},
      )

    )
  }






  // "ADMIN"




  @cask.get("/")
  def hello(errorOpt: Option[String] = None,
                            userName: Option[String] = None,
                            msg: Option[String] = None
                           ) = {
    html(
      head(
        meta(charset:="UTF-8"),
        link(
          rel := "stylesheet",
          href := "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css",

          src := "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Atria_Jauhelihapizza.jpg/1280px-Atria_Jauhelihapizza.jpg"


        ),
          scalatags.Text.tags2.title("Pizza orders n sheet")

      ),


      body(
        meta(charset:="UTF-8"),
        style := "background-image: url(https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Atria_Jauhelihapizza.jpg/1280px-Atria_Jauhelihapizza.jpg)",


        div(cls := "container")(
          h1("Pizza orders n sheet "), // removed Admin View
          h2(s"Please enter your order.\nYour order is number #"+yourNumber.toString),
          //h3(tilaukset_stringinä),
          hr,
          div(id := "Orderlist") (
            messageList_Customer()
          )),

          hr,


          for(error <- errorOpt)
          yield i(color.red)(error),


          // Tässä tulee se pääasiallinen form
          form(action := "/", method := "post")(
            meta(charset:="UTF-8"),


            input(`type` := "push", placeholder := yourNumber.toString,disabled:="disabled", width := "20%"),
            input(`type` := "text", name := "name",
            placeholder := "Name", width := "10%",
            userName.map( value := _) ),
            input( `type`:= "text", name := "msg", placeholder := "Please write a your order!", width:="40%", // possible to do Tilauksen jonkinlaisena valikkona
            msg.map(value := _)),
            select( id:= "receipt", name:="receipt", // FORM ASKS THIS, DOES NOT CHANGE ANYTHING
                option("No receipt needed", value:= "No receipt needed"),
                option("Receipt does not matter", value:= "Receipt does not matter"),
                option("Receipt wanted, not given", value:= "Receipt wanted, not given"),
                  ),

            select( id:= "maksutapa", name:= "maksutapa",
                option("cash", value:="cash"),
                option("card", value:= "card"),
                option("other", value:="other"),
                  ),
            input(`type` := "submit", width := "10%")
          ),
        a(href:="/admin", "To Admin page"), // changed from customer

      if (tilaus_vector.exists(_.toString.contains("Rick")) ||tilaus_vector.exists(_.toString.contains("rick"))) {embed},
      )

    )
  }

  initialize()
}
