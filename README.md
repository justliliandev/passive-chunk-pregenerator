# Passive Chunk Pregenerator
This project is build using the multiloader template.
For more information see the readme of it.
This mod generates chunks passively around players, offline or online, and the worldspawn to generate chunks that will most likely be needed in the near future.


## How this works
Minecraft is partially multithreaded.<br>
Each Dimension(or Level if you are a modder and use Mojmaps) has its own Thread to generate chunks.
On the Main Thread, the one used for most ingame stuff, I'm checking for viable chunks to load. This includes a check if that dimensions worldgen thread is currently generating a chunk. This makes sure, that more important chunks are generated first. It then schedules the generation of a viable chunk on each thread, if it could find one.

## Error Suppression
The IO Thread is sometimes trying to save chunks, while a new one is added to a map by a worldgen thread. This causes ConcurrentModificationExceptions to be thrown and logged. The chunk is saved to disc in a later attempt (that happened during my testing, and shouldn't destroy worlds (but you should make backups, just to be sure)). These Exception, because they happen rather often and can be ignored are suppressed and won't be logged.
