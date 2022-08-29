package app
import org.w3c.dom.html.HTMLOptGroupElement
import scalatags.Text.all.{input, _}

import java.util.Calendar

object MinimalApplication extends cask.MainRoutes{
// can be found at https://github.com/teeriav1/server

  class Tilaus(val order_nro: Int, val customer: String,val food: String){

      override def toString: String ="#"+order_nro.toString +" to "+ customer+": "+ food + " at "+ time_promised


      // TIME ACCESS / laskee luvattua aikaa
      val tilattu = Calendar.getInstance().getTime
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
        time_promised ="Order nro:"+this.order_nro.toString+" ready at: "+ luvattu_hour.toString+"."+luvattu_mins.toString
      }
      calculate_time_promised


    }
      // Paikka säilöä tehdyt tilaukset
      var tilaus_vector: Vector[ Tilaus]= Vector[Tilaus]()
      def tilaukset_stringinä = {
        var palautusString = ""
        if (tilaus_vector.length > 0){
        //tilaus_vector.foreach( palautusString +=_.time_promised+"\n") tää palauttaa sellaisen näkymän mikä voi näkyä kaikille
        tilaus_vector.foreach( palautusString+= _+"\n") // admin näkynä
        palautusString
      }else{""}}

      // yourNumber calculates the number of next order
      def yourNumber: Int = tilaus_vector.length +1
      var messages = {Vector(
        ("", ""),
        ("", ""),
      )}
  // Tää roska poistuu kun lakkaa auttamasta debuggia
  def messageList() = {
    frag(
      for((customer, food) <- messages)
      yield p(b(customer), " ", food)
    )
  }
// Tää roska poistuu kun lakkaa auttamasta debuggia / end

  // postHello aka tilauksen käsittely/ formin lähetys
  @cask.postForm("/")
  def postHello(name: String, msg: String) = {
    if (name == "") hello(Some("Name cannot be empty"), Some(name), Some(msg))
    else if (name.length >= 20) hello(Some("Name cannot be longer than 20 characters"), Some(name), Some(msg))
    else if (msg == "") hello(Some("Message cannot be empty"), Some(name), Some(msg))
    else if (msg.length >= 160) hello(Some("Message cannot be longer than 160 characters"), Some(name), Some(msg))
    else {
      messages = messages :+ (name -> msg)
      this.tilaus_vector = this.tilaus_vector :+ new Tilaus(yourNumber, name, msg)
      hello(None, None, None)

    }
  }
  // "MAIN"
  @cask.get("/")
  def hello(errorOpt: Option[String] = None,
                            userName: Option[String] = None,
                            msg: Option[String] = None
                           ) = {
    html(
      head(
        link(
          rel := "stylesheet",
          href := "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        )
      ),
      body(
        div(cls := "container")(
          h1("Pizza orders n sheet"),
          h2(s"Please enter your order.\nYour order is number #"+yourNumber.toString),
          h3(tilaukset_stringinä),
          hr,
          div(id := "Orderlist") (
            //messageList()
          )),
          hr,
          for(error <- errorOpt)
          yield i(color.red)(error),


          // Tässä tulee se pääasiallinen form
          form(action := "/", method := "post")(
            input(`type` := "push", placeholder := yourNumber.toString,disabled:="disabled", width := "20%"),
            input(`type` := "text", name := "name",
            placeholder := "Name", width := "10%",
            userName.map( value := _) ),
            input( `type`:= "text", name := "msg", placeholder := "Please write a your order!", width:="40%", // possible to do Tilauksen jonkinlaisena valikkona
            msg.map(value := _)),
            //input(`type`:= select:HTMLOptGroupElement, name := "maksutapa", value := "kortti", value := "kateinen", width:= "20%"), // TODO maksutapa dropdown valikkona

            input(`type` := "submit", width := "10%")
          )

      )

    )
  }

  initialize()
}