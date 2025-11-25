package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.ChecklistCategory
import com.example.hollow_knight_silkroad.Model.ChecklistItem

object ChecklistRepository {
    val percentageValues = mapOf(
        "boss" to 1.0,
        "equip" to 2.0,
        "spell" to 1.0,
        "colosseum" to 1.0,
        "mask-shard" to 0.25,
        "nail-upgrade" to 1.0,
        "dream-nail" to 1.0,
        "nail-art" to 1.0,
        "vessel-fragment" to 1.0 / 3.0,
        "dreamer" to 2.0,
        "godhome" to 1.0,
        "charm" to 1.0
    )
    val allCategories: List<ChecklistCategory> = listOf(
        ChecklistCategory(
            category = "boss", title = "Jefes", items = listOf(
                ChecklistItem(id = "boss1", label = "Falso Caballero", category = "boss"),
                ChecklistItem(id = "boss2", label = "Madre Gruz", category = "boss"),
                ChecklistItem(id = "boss3", label = "Maestro del alma", category = "boss"),
                ChecklistItem(id = "boss4", label = "Malwek Incubador", category = "boss"),
                ChecklistItem(id = "boss5", label = "Señores Mantis", category = "boss"),
                ChecklistItem(id = "boss6", label = "Hornet (Protectora)", category = "boss"),
                ChecklistItem(id = "boss7", label = "Nosk", category = "boss"),
                ChecklistItem(id = "boss8", label = "Caballero de la Colmena", category = "boss"),
                ChecklistItem(id = "boss9", label = "Defensor del Estiercol", category = "boss"),
                ChecklistItem(id = "boss10", label = "Hornet (Centinela)", category = "boss"),
                ChecklistItem(id = "boss11", label = "Señor Traidor", category = "boss"),
                ChecklistItem(id = "boss12", label = "Grim", category = "boss"),
                ChecklistItem(id = "boss13", label = "Coleccionista", category = "boss"),
                ChecklistItem(id = "boss14", label = "Uumuu", category = "boss"),
                ChecklistItem(id = "boss15", label = "Caballeros Vigilantes", category = "boss"),
                ChecklistItem(id = "boss16", label = "Galien", category = "boss"),
                ChecklistItem(id = "boss17", label = "Marmu", category = "boss"),
                ChecklistItem(id = "boss18", label = "Markoth", category = "boss"),
                ChecklistItem(id = "boss19", label = "Xero", category = "boss"),
                ChecklistItem(id = "boss20", label = "Sin Ojos", category = "boss"),
                ChecklistItem(id = "boss21", label = "Viejo Hu", category = "boss"),
                ChecklistItem(id = "boss22", label = "Gorb", category = "boss"),
                ChecklistItem(id = "boss23", label = "Rey Pesadilla Grimm", category = "boss")
            )
        ),
        ChecklistCategory(
            category = "equip", title = "Equipamiento", items = listOf(
                ChecklistItem(id = "equip1", label = "Capa de Polilla", category = "equip"),
                ChecklistItem(id = "equip2", label = "Garra de Mantis", category = "equip"),
                ChecklistItem(id = "equip3", label = "Corazon de Cristal", category = "equip"),
                ChecklistItem(id = "equip4", label = "Lagrima de Isma", category = "equip"),
                ChecklistItem(id = "equip5", label = "Marca del Rey", category = "equip"),
                ChecklistItem(id = "equip6", label = "Alas del Monarca", category = "equip"),
                ChecklistItem(id = "equip7", label = "Manto de la Oscuridad", category = "equip")
            )
        ),
        ChecklistCategory(
            category = "spell", title = "Hechizos", items = listOf(
                ChecklistItem(id = "spell1", label = "Espiritu Vengativo", category = "spell"),
                ChecklistItem(id = "spell2", label = "Alma Sombria", category = "spell"),
                ChecklistItem(id = "spell3", label = "Oscuridad descendente", category = "spell"),
                ChecklistItem(id = "spell4", label = "Espectros Aulladores", category = "spell"),
                ChecklistItem(id = "spell5", label = "Chillido del Abismo", category = "spell")
            )
        ),
        ChecklistCategory(
            category = "colosseum", title = "Coliseo", items = listOf(
                ChecklistItem(id = "colosseum1", label = "Prueba del Guerrero", category = "colosseum"),
                ChecklistItem(id = "colosseum2", label = "Prueba del Conquistador", category = "colosseum"),
                ChecklistItem(id = "colosseum3", label = "Prueba del Insensato", category = "colosseum")
            )
        ),
        ChecklistCategory(
            category = "mask-shard", title = "Fragmentos de Mascara", items = listOf(
                ChecklistItem(id = "mask-shard1", label = "Fragmento 1", category = "mask-shard"),
                ChecklistItem(id = "mask-shard2", label = "Fragmento 2", category = "mask-shard"),
                ChecklistItem(id = "mask-shard3", label = "Fragmento 3", category = "mask-shard"),
                ChecklistItem(id = "mask-shard4", label = "Fragmento 4", category = "mask-shard"),
                ChecklistItem(id = "mask-shard5", label = "Fragmento 5", category = "mask-shard"),
                ChecklistItem(id = "mask-shard6", label = "Fragmento 6", category = "mask-shard"),
                ChecklistItem(id = "mask-shard7", label = "Fragmento 7", category = "mask-shard"),
                ChecklistItem(id = "mask-shard8", label = "Fragmento 8", category = "mask-shard"),
                ChecklistItem(id = "mask-shard9", label = "Fragmento 9", category = "mask-shard"),
                ChecklistItem(id = "mask-shard10", label = "Fragmento 10", category = "mask-shard"),
                ChecklistItem(id = "mask-shard11", label = "Fragmento 11", category = "mask-shard"),
                ChecklistItem(id = "mask-shard12", label = "Fragmento 12", category = "mask-shard"),
                ChecklistItem(id = "mask-shard13", label = "Fragmento 13", category = "mask-shard"),
                ChecklistItem(id = "mask-shard14", label = "Fragmento 14", category = "mask-shard"),
                ChecklistItem(id = "mask-shard15", label = "Fragmento 15", category = "mask-shard"),
                ChecklistItem(id = "mask-shard16", label = "Fragmento 16", category = "mask-shard")
            )
        ),
        ChecklistCategory(
            category = "nail-upgrade", title = "Mejoras de Clavo", items = listOf(
                ChecklistItem(id = "nail-upgrade1", label = "Aguijon Afilado", category = "nail-upgrade"),
                ChecklistItem(id = "nail-upgrade2", label = "Aguijon Esterilizado", category = "nail-upgrade"),
                ChecklistItem(id = "nail-upgrade3", label = "Aguijon en Espiral", category = "nail-upgrade"),
                ChecklistItem(id = "nail-upgrade4", label = "Aguijon Puro", category = "nail-upgrade")
            )
        ),
        ChecklistCategory(
            category = "dream-nail", title = "Nail de Sueño", items = listOf( // Consider renaming title to "Aguijón Onírico"
                ChecklistItem(id = "dream-nail1", label = "Consigue el Aguijon Onirico", category = "dream-nail"),
                ChecklistItem(id = "dream-nail2", label = "Despierta el Aguijon Onirico", category = "dream-nail"),
                ChecklistItem(id = "dream-nail3", label = "Ultimas palabras del Seer", category = "dream-nail")
            )
        ),
        ChecklistCategory(
            category = "nail-art", title = "Arte del aguijon", items = listOf(
                ChecklistItem(id = "nail-art1", label = "Gran Corte", category = "nail-art"),
                ChecklistItem(id = "nail-art2", label = "Corte Ciclon", category = "nail-art"),
                ChecklistItem(id = "nail-art3", label = "Corte Veloz", category = "nail-art")
            )
        ),
        ChecklistCategory(
            category = "vessel-fragment", title = "Fragmentos de Vasija", items = listOf(
                ChecklistItem(id = "vessel-fragment1", label = "Fragmento de Vasija 1", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment2", label = "Fragmento de Vasija 2", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment3", label = "Fragmento de Vasija 3", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment4", label = "Fragmento de Vasija 4", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment5", label = "Fragmento de Vasija 5", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment6", label = "Fragmento de Vasija 6", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment7", label = "Fragmento de Vasija 7", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment8", label = "Fragmento de Vasija 8", category = "vessel-fragment"),
                ChecklistItem(id = "vessel-fragment9", label = "Fragmento de Vasija 9", category = "vessel-fragment")
            )
        ),
        ChecklistCategory(
            category = "dreamer", title = "Soñadores", items = listOf(
                ChecklistItem(id = "dreamer1", label = "Monomon la Entendida", category = "dreamer"), // Adjusted name
                ChecklistItem(id = "dreamer2", label = "Lurien el Vigia", category = "dreamer"),
                ChecklistItem(id = "dreamer3", label = "Herrah la Bestia", category = "dreamer")
            )
        ),
        ChecklistCategory(
            category = "godhome", title = "Hogar de los Dioses", items = listOf(
                ChecklistItem(id = "godhome1", label = "Panteon del Maestro", category = "godhome"),
                ChecklistItem(id = "godhome2", label = "Panteon del Artista", category = "godhome"),
                ChecklistItem(id = "godhome3", label = "Panteon del Sabio", category = "godhome"),
                ChecklistItem(id = "godhome4", label = "Panteon del Caballero", category = "godhome"),
                ChecklistItem(id = "godhome5", label = "Panteon de Hallownest", category = "godhome")
            )
        ),
        ChecklistCategory(
            category = "charm", title = "Amuletos", items = listOf(
                ChecklistItem(id = "charm1", label = "Brujula Caprichosa", category = "charm"),
                ChecklistItem(id = "charm2", label = "Enjambre Recolector", category = "charm"),
                ChecklistItem(id = "charm3", label = "Coraza Robusta", category = "charm"),
                ChecklistItem(id = "charm4", label = "Atrapaalmas", category = "charm"),
                ChecklistItem(id = "charm5", label = "Piedra de Chamán", category = "charm"),
                ChecklistItem(id = "charm6", label = "Devoralmas", category = "charm"),
                ChecklistItem(id = "charm7", label = "Maestro de la Embestida", category = "charm"),
                ChecklistItem(id = "charm8", label = "Maestro del Esprint", category = "charm"),
                ChecklistItem(id = "charm9", label = "Cancion de las Larvas", category = "charm"),
                ChecklistItem(id = "charm10", label = "Elegía de la Larvamosca", category = "charm"),
                ChecklistItem(id = "charm11", label = "Corazon Fragil", category = "charm"),
                ChecklistItem(id = "charm12", label = "Avaricia Fragil", category = "charm"),
                ChecklistItem(id = "charm13", label = "Fuerza Fragil", category = "charm"),
                ChecklistItem(id = "charm14", label = "TuerceHechizos", category = "charm"),
                ChecklistItem(id = "charm15", label = "Cuerpo Firme", category = "charm"),
                ChecklistItem(id = "charm16", label = "Golpe Pesado", category = "charm"),
                ChecklistItem(id = "charm17", label = "Corte Rápido", category = "charm"),
                ChecklistItem(id = "charm18", label = "Largoaguijon", category = "charm"),
                ChecklistItem(id = "charm19", label = "Marca del Orgullo", category = "charm"),
                ChecklistItem(id = "charm20", label = "Furia de los Caídos", category = "charm"),
                ChecklistItem(id = "charm21", label = "Espinas de Agonía", category = "charm"),
                ChecklistItem(id = "charm22", label = "Coraza de Baldur", category = "charm"),
                ChecklistItem(id = "charm23", label = "Nido de Larvas", category = "charm"),
                ChecklistItem(id = "charm24", label = "Blason del Defensor", category = "charm"),
                ChecklistItem(id = "charm25", label = "Vientre Brillante", category = "charm"),
                ChecklistItem(id = "charm26", label = "Concentracion Rapida", category = "charm"),
                ChecklistItem(id = "charm27", label = "Concentracion Profunda", category = "charm"),
                ChecklistItem(id = "charm28", label = "Corazón de Sangre Vital", category = "charm"),
                ChecklistItem(id = "charm29", label = "Alma de Sangre Vital", category = "charm"),
                ChecklistItem(id = "charm30", label = "Bendicion de Joni", category = "charm"),
                ChecklistItem(id = "charm31", label = "Sangrecolmena", category = "charm"),
                ChecklistItem(id = "charm32", label = "Hongo con Esporas", category = "charm"),
                ChecklistItem(id = "charm33", label = "Sombra Afilada", category = "charm"),
                ChecklistItem(id = "charm34", label = "Forma de Unn", category = "charm"),
                ChecklistItem(id = "charm35", label = "Gloria del Maestro de Aguijones", category = "charm"),
                ChecklistItem(id = "charm36", label = "Cancion de la Tejedora", category = "charm"),
                ChecklistItem(id = "charm37", label = "Portador Onirico", category = "charm"),
                ChecklistItem(id = "charm38", label = "Escudo Onirico", category = "charm"),
                ChecklistItem(id = "charm39", label = "Niño de Grimm / Melodia Despreocupada", category = "charm"),
                ChecklistItem(id = "charm40", label = "Alma del Rey / Corazón del vacio", category = "charm")
            )
        )
    )
    val allItems: List<ChecklistItem> = allCategories.flatMap { it.items }
}