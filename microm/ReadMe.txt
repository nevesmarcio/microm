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
	- texturas coincidentes / - tecnica do "thing"
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
# pq é que não aparecem a ball e o thing ??? --> tinha a ver com a ordem de criação dos objectos poder acontecer antes dos limites fisicos do board
# testes no droid

21-02-2013
# se tiver o textureAtlas com uma página demasiado larga/ alta, as texturas ficam todas a branco, como que se não fossem POT (n funciona com 8192; funciona com 2048)


22-02-2013
23-02-2013
# refactorização de todo o carregamento do nível a partir do SVG e da classe BasicShape (tentativa de problema da abstração dos shapes e propagação dos scalings, etc...)
# OPENGL CONTEXT:: 
	os model remetem um handler a ser invocado após o step (pelo wmManager.process()), que corre na thread do Timer0
	é este handler que avisa a partir de um evento que o construtor está pronto
	é em resposta a este evento que o DelayedInit das views views são construidas
	assim, o delayedInit corre na thread do Timer0. É preciso então garantir que o que diz respeito ao OPENGL Context que ocorre no delayedInit é invocado na thread do GUI.
	(ver exemplo do PortalView onde tem o Gdx.app.postRunnable())
# Bug da textura da mesh depois de a ter enfiado no atlas (terá a ver com as camaras e a renderização do mesh ?)
	Já percebi o que se passa. A textura é a inteira. O trabalho de "trim" tem que ser feito no UVMAP de acordo com a region (o que faz todo o sentido, né...) 


27-02-2013
# separar as opções de renderização em variaveis globais (dev elements, shapes, textures and particle system)
# trabalho na forma de texturização mais "low-level" (UV-MAPS, OPENGL, PROJECTION/ MODEL matrices)
	-- checked: não é possível repetir uma sub-textura (http://stackoverflow.com/questions/662107/how-to-use-gl-repeat-to-repeat-only-a-selection-of-a-texture-atlas-opengl)
# todos os allocations que acontecem em pleno funcionamento da APP, não deverão acontecer!! (certo??)
	(acho que descobri uma forma fixe de olhar para a APP do ponto de vista da optimização: em pleno funcionamento a app não deverá fazer alocações, pelo que essa análise a partir de um profiler é mto importante!!!


28-02-2013
# Texturização do Spawn e do Goal
# Diversas optimizações na alocação de memória (GameTickGenerator p.e.)
	:: passei os seguintes blocos para a categoria "DEV ELEMENTS"
		-Renderização de contactor: WorldView --> addAll 
		-FPS/ UPS: UiView --> StringBuilder
# Adicionado o mecanismo de "Screens" e refactorização necessária


01-03-2013
# integrado o scene2d.ui - mecanismo de navegação na app
	- Splash screen 	(arranque da app)
	- Main screen 		(landing screen)
	- World select		(onde se selecciona o tema/ schema/ world)
	- Level Select		(onde se selecciona o nível dentro de um tema)
	- Pause overlay		(quando o user carrega no back-key)
	- Level success		(nível superado/ ou não)

04-03-2013
# Level Design: diminuido o tamanho da Box e modificada a força do impulso e velocidade constante
# Criadas classes para o Splash, Menu, World Select e GamePause e implementado o fluxo básico entre os ecrãns

05-03-2013
# desenho... procura de inspiração para 1 tema coerente

20-03-2013
# Técnica de animação 1 - spritesheet: spritesheet implementada no loading screen com uma exportação válida do flash cs6 :) -- cool!!

22-03-2013
# Técnica de animação 2 - tweens: relvinha ...


(in progress)
# Construir o splashscreen!!

Assumir uma resolução e partir daí, com downscale/ upscale











[TODO] 
# como faço o level design quando for um horizontal scroller ?

# counter de vidas e mecanismo de restart no mesmo nivel
# zona de texto do UI para contar as vidas/ saltar para o menu, etc.


# tutorial da relva (usa os tweens ?)
# particles na destruição

# efeito de paralax


# ao fazer "resume" das apps recentes, as texturas ficam todas paridas.
	
# use a chipin like feature to raise money for more levels (fundraising web sites:: http://www.squidoo.com/fundraising-websites)

# que coordenadas utilizar aquando a escrita das mensagens no UI? (fisicas? relativas ao tamanho do ecrã? outro?)



# detectar o level success e apresentar o próximo nível

# como desenhar as texturas para os tamanhos? relação entre dimensões e resoluções



[BUGS]
# ao fazer rotate o OPENGL parte-se todo. averiguar pq. Para resolver, forçei o layout a landscape na app android
# A cópia das matrizes ".cpy()" no portalView aloca memoria de embarda...


[ideias]
# sistema de niveis com percentagens e temas tipo o blast the monkey, ou o cut the ropee
# usar a facilidade de port do libgdx para o browser para fazer hosting de games (network) and bets and stuff