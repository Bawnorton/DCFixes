package dcfixes.utils

import net.minecraftforge.srgutils.IMappingFile
import net.minecraftforge.srgutils.INamedMappingFile
import java.io.File

object MappingBridge {
  fun createIntermediaryToMojmap(intermediaryTiny: File, mojangClientMappings: File, outputTiny: File) {
    val namedMappings = INamedMappingFile.load(intermediaryTiny)
    val intermediaryToOfficial = namedMappings.getMap("intermediary", "official")
    val mojangToOfficial = IMappingFile.load(mojangClientMappings)

    val intermediaryToMojmap = intermediaryToOfficial.chain(mojangToOfficial.reverse())

    outputTiny.parentFile.mkdirs()
    intermediaryToMojmap.write(outputTiny.toPath(), IMappingFile.Format.TINY, false)
  }
}

