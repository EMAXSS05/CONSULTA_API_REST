import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class Country(
    val name: CountryName,
    val region: String,
    val population: Long,
    val area: Double
)

@Serializable
data class CountryName(
    val common: String
)

fun main() {

    val client = HttpClient.newHttpClient()


    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://restcountries.com/v3.1/all"))
        .GET()
        .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())


    // Deserializar el JSON en una lista de países
    val json = Json { ignoreUnknownKeys = true }
    val countries: List<Country> = json.decodeFromString(response.body())


    println("\nPaíses ordenados por población descendente:")
    val sortedByPopulation = countries.sortedByDescending { it.population }
    sortedByPopulation.take(10).forEach {
        println("${it.name.common} - ${it.population} habitantes")
    }


    println("\nLista de nombres de 10 países")
    val countryNames = countries.map { it.name.common }
    println(countryNames.take(10))

    println("\nAgrupados por región:")
    val groupedByRegion = countries.groupBy { it.region }.forEach { (region, list) ->
        println("$region: ${list.size} países")
    }

    println("\nPaíses que empiezan con 'P':")
    val filteredCountries = countries.filter { it.name.common.startsWith("P") }.forEach { println(it.name.common) }


}
