MicroMachines clone

Physics Editor
http://code.google.com/p/box2d-editor/
http://www.aurelienribon.com/blog/projects/physics-body-editor/
http://www.aurelienribon.com/blog/projects/universal-tween-engine/
http://code.google.com/p/libgdx-texturepacker-gui/


[DONE]
# code review - organizar a arquitectura
# implementados os pickrays para mais do que um ponto de toque no ecrã
# averiguar o porquê dos flicks do ecrã (que se manifestam mais no droid, mas tb acontecem no PC) :: tem a ver com o camera-update no cameraModel para forçar o update após alteração da camara
# integrar os tweens

# volta e meia a coisa tem um crash feio (desconfio que é por leituras/ alterações a "zonas criticas" - box2d)
  ::" Box2d locks the world while it dispatches events BEGIN_CONTACT, END_CONTACT, PRE_SOLVE and POST_SOLVE. "
  ::implementei uma forma de adicionar/ remover objectos no fim do calculo do step do physics engine.

# introduzi o xpath para ler niveis a partir de um svg
# introduzi as regex para conseguir fazer parse do svg

21-01-2013
# criei novo tipo de modelo para representar o "ground"

22-01-2013
# movimento na "daBox" e mapeamento do Key.SPACE

24-01-2013
# colision detection com os portais

27-01-2013
# resolução do problema do level loading e do centro do objecto (box2d)

28-01-2013
# implementação parcial das barreiras (walls)
# refactorizações

29-01-2013
# implementação parcial do handler de start (spawn de bolas) 
# implementação das paredes
# handler para o start
# handler para o finish
# handler para a destruição do objecto

30-01-2013
# read about textures meshes and stuff.... fuck!!!!!!!!!! --> tests on PortalView 

01-02-2013
# textureAtlas
# tween accessor

05-02-2013
# filtros sobre as texturas; conceitos: texel vs pixel, linear filter, nearest filter, mipmap(?), pixelperfect

07-02-2013
# mais umas investidas nos tipos de ecrãs e no cálculo dos parâmetros do CameraModel

08-02-2013
# BUG: resolvido o problema dos file handles e do Gdx.files.internal (não posso usar um file handle com o "internal"; usei o inputstream)

09-02-2013
# BUG?: out of memory? (avaliar este problema!!!)
	-CameraView --> esta vista não tem nada
	-UiView --> penso que é esta vista que provoca os sucessivos GCs
	-WorldView
	-GridView
	>> todas as vistas tem problemas de memory allocation
>> o problema estava na forma de escrita dos logs: apesar de estarem configurados para excluir os logs, as strings eram construidas na mesma matando a performance e a memória

11-02-2013
# implementado o mecanismo de logging como deve ser: a verificar o nivel de logs activo para impedir o string building todo que acontecia
# o objecto Camera, agora já tem uma camera 2D para o desenho do UI
# alterado o level loader: o scale foi movido para um local mais apropriado do código (fora dos "add")

12-02-2013
# refactorizado o rendering de fps/ ups
# DaBox passou a ser construido de acordo com o svg
	- atenção que chainshapes não colidem entre si: o dabox é um polygonshape (tem "volume" - é mais tem área, mas pronto)
	- pequenas alterações na dimensão da caixota fazem com que a força do jump tenha que ser recalculada (isto não está automático)

13-02-2013
# DaBox está agora relacionada com o spawning point: é colocada em jogo por este, e a sua posição inicial é a do spawning point
# Implementado o mecanismo de tweening sobre o UI no que respeita a texto (tenho um tech debt ENORME para pagar!)

14-02-2013 (ai jasus o tech debt...)
# Removidas as instâncias do flashmessage no fim da animação prevista
# Implementado um countdown para a Dabox saltar do spawning point
# exemplo de particles integradas 

15-02-2013
16-02-2013
17-02-2013
18-02-2013
# começar a debulhar o look da coisa (implementar o protótipo do inkscape)
	- texturas coincidentes / - tecnica do "Coisa"
	- 9.patches ? 
	- mesh (preciso de mais valores para o UV map --> svg ?) :: no fundo a técnica do coisa faz isto mas incorporado com o box2d
	- texturas à medida e scales apropriados

convencionar que as meshes são sempre boxes para ser facil texturar?

19-02-2013
# texturização dos objectos (tech debt...)
	- Dabox
	- Walls
	- Board
# problema do z-index e da ordem de instanciação (dá para resolver facilzinho ordenando a lista de disparos do render!!) --> resolvido de forma brilhante. Às vezes tenho umas ideias de cagalhão. Assim sim!

20-02-2013
# Uniformização dos métodos do level loader para passarem apenas as shapes ao invés da lista de pontos
# Texturização dos grounds
# particles a seguir a caixa e mais pequenas 
# coisa model passa a atender ao offset para determinar a coordenada de criação
# transparecer as exceptions da reflexão das views para ser mais facil o debug de problemas
# Texture atlas + enforce POT --> fazer o carregamento das texturas com base em atlas

(in progress...)
# bug da textura da mesh
# testes no droid


[TODO] 
# que coordenadas utilizar aquando a escrita das mensagens no UI? (fisicas? relativas ao tamanho do ecrã? outro?)

# Mecanismo de navegação na app
	- Splash screen 	(arranque da app)
	- Main screen 		(landing screen)
	- World select		(onde se selecciona o tema/ schema/ world)
	- Level Select		(onde se selecciona o nível dentro de um tema)
	- Pause overlay		(quando o user carrega no back-key)
	- Level success		(nível superado/ ou não)

# detectar o level success e apresentar o próximo nível

# como desenhar as texturas para os tamanhos? relação entre dimensões e resoluções

# counter de vidas e mecanismo de restart no mesmo nivel
# zona de texto do UI para contar as vidas/ saltar para o menu, etc.


# tutorial da relva (usa os tweens ?)
# particles na destruição

# efeito de paralax


[BUGS]
# ao fazer rotate o OPENGL parte-se todo. averiguar pq. Para resolver, forçei o layout a landscape na app android
# BUG?: problema das texturas (enforce POT?)
# nas colisões há um glitch esquisito


[ideias]
# sistema de niveis com percentagens e temas tipo o blast the monkey, ou o cut the ropee
# usar a facilidade de port do libgdx para o browser para fazer hosting de games (network) and bets and stuff