OneButton Game

Physics Editor
http://code.google.com/p/box2d-editor/
http://www.aurelienribon.com/blog/projects/physics-body-editor/
http://www.aurelienribon.com/blog/projects/universal-tween-engine/
http://code.google.com/p/libgdx-texturepacker-gui/


[DONE]
16-01-2013
# estrutura inicial do projecto +/- estabilizado do ponto de vista da arquitectura

17-01-2013
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
# Técnica de animação 2 - tweens: relvinha ...

23-03-2013
# Lore design
# Level design: world 1 (s1.0 s1.1 s1.2 s1.3 s1.4 s1.5 s1.6 s1.10)  }} falta o s1.7 s1.8 e s1.9

24-03-2013
# implementação da star
# Implementada a leitura do offset (meia ranhosa, mas safa para já)
# Refactorizada a estrutura de classes Model, no entanto ainda não tenho a estrutura estabilizada... o BodyInterface está a expôr as fixtures e a BasicShape... Uma das exposições tem que cair.

25-03-2013
# implementação das particulas da star

26-03-2013
# Lógica de apanha da estrela (com destruição do objecto)

27-03-2013
# implementar o texto visível
# problema das coordenadas... fdx... tanto xanatanço

28-03-2013
# mais xanatanço com força no que respeita às coordenadas do texto...
# implementação do objecto SimpleTrigger
# integrado o rhino (javascript engine) :: a funcionar em desktop e também no droid wOOOt!!


09-04-2013
10-04-2013
# ClassicSingleton
  Da forma que tenho esta classe implementada estou a contar com o thread do UI para pendurar o contexto do javascript, o que não me parece fixe!
# Já agora, não tenho uma classe qualquer onde penduro "ponteiros para funções" ? Já que há um loop baseado num timer, este não deveria aceitar runnables ?? DEVERIA!!!
  Ver o exemplo do libgdx - https://github.com/libgdx/libgdx/blob/master/backends/gdx-backend-lwjgl/src/com/badlogic/gdx/backends/lwjgl/LwjglApplication.java

11-04-2013
# Identificação dos estouros da VM:
	. a criação de joints com o rato dentro de um step
	. renderização dos contactos no WorldView.java ?

# Implementação de um mecanismo de correr runnables nas 2 threads princiais da aplicação: ScreenTickManager e GameTickGenerator
# Refactorização da nomenclatura dos interfaces (ISomeInterface)
# Motor do DaBoxModel implementado com recurso a impulsos e não um set da linearVelocity à bruta

12-04-2013
# Mecanismo de notificações de colisões

14-04-2013
15-04-2013
# Explorado o mecanismo dos Enums mais a fundo no java: http://stackoverflow.com/questions/2770321/what-is-a-raw-type-and-why-shouldnt-we-use-it
# Refactorizações ao mecanismo de notificação de colisões e bridge com o motor de javascript

16-04-2013
# Finalmente deleguei o javascript para os elementos do tipo SimpleTriggerModel
# Prova de conceito do javascript terminada. Agora é ir extendendo a API para permitir lógica no javascript

17-04-2013
18-04-2013
# Refactorizado PointerToFunctions para ICommand (interface) --> command design pattern
# Refactorização do mecanismo de salto entre Screens
# Sistema de loading de worlds(folder) e levels(files)

19-04-2013
# procurar os singletons manhosos - removi o "anti-pattern" singleton do WorldModel. 
	http://caines.ca/blog/programming/singletons-anti-pattern-or-worst-anti-pattern-ever/
	http://javapeanuts.blogspot.pt/2012/02/singleton-testing-and-dependency.html

20-04-2013
22-04-2013
23-04-2013
24-04-2013
# Mecanismo de carregamento / loading (E mais importante que isso, o mecanismo de unloading (dispose))
	- Navegação entre menus já não deixa referências penduradas que impedem o GC
	- Avaliar: https://weblogs.java.net/blog/2006/05/04/understanding-weak-references
# Unloading do "theJuice"
# Enums e statics não são gc'ed. O classloader fica com referência para eles.
# interruptible threads (JAVA NIO: doesn't work on System.in)

26-04-2013
# substituição do timer do gameTickGenerator para um ScheduledThreadPool
# O código faz o unload todo, mas está assim um bocado ratado (para não dizer muito ratado)

30-04-2013
# Desenvolvimento de funções para expor à API javascript: procura de elemento por nome e listagem de elementos
# Diversas refactorizações
# Introdução de um exemplo simples de move de uma wall aquando a passagem num trigger (level#1.1)

01-05-2013
# adicionada a lib gson para serialize/deserialize

02-05-2013
# mecanismo de save/load do progresso do jogador

03-05-2013
# Criação de uma estrutura em memória que guarda os worlds/ levels disponíveis
# Adaptação do mecanismo de save à estrutura de memória (sync)
# inicio da definição da API que permite interagir com os scores

04-05-2013
# Separação da API em 2 classes: AchievementService e ScreenFlowService
# Criação de um SessionState (para guardar o modo seleccionado, mute, etc.) 
# Workflow simples de uma vez terminado um level, voltar ao menu --> está mto xanatado, mas vou deixar para já, para depois voltar a esta questão com uma perspectiva mais critica.

[[[[[[[ tentar abordar temas como lighting / shadow para desenjoar dos workflows/ GC's, etc.!!! ]]]]]]]
# introduzida a lib das luzes :: https://code.google.com/p/box2dlights/


06-05-2013
# Very cool art style: http://www.gamerprint.co.uk/collections/hyrulean-travel (tb gosto do art style super simplificado. tenho um artigo no pocket)
# implementado um novo tipo de objecto que dará origem aos magnets (objectos repelidos pela daBox)


07-05-2013
---technology evaluation day---
# fluids
	http://www.youtube.com/watch?v=dCxmS5IrMRo
	https://plus.google.com/104301319754911100519/posts/4BaC8MJC7UP
	http://www.jbox2d.org/liquid/

# dynamic loading example
	http://www.youtube.com/watch?v=pzO1AsL2ZXk

# post processing filters
	http://manuelbua.github.io/libgdx-contribs/
---inspiration titles---
*thomas was alone
*super splatters
*flat design era
*dots (ios) - simple and elegant graphics
*they need to be fed 2 - gravitational field

08-05-2013
# adicionada estrutura de testes unitários ao projecto (avaliar TestNG)
# suporte para pintar os polígonos de acordo com a cor definida no inkscape
	* ainda n consegui fazer o valor do alpha da cor, surtir efeito
	* também é necessário conseguir fazer um post-processing para dar o efeitozinho do sombreado para dar alguma profundidade
	(GLSL post processing?)

# minimal convex polygon
http://valis.cs.uiuc.edu/~sariel/research/CG/applets/convex_decomp/Default.html
# porque nao usar triangulação em todos?
http://stackoverflow.com/questions/5646221/delaunay-triangulating-the-2d-polygon-with-holes


09-05-2013
# refactorizações:
	* ver o canvas de forma diferente (sem a complicação dos offsets): usar um mecanismo diferente para localizar a camera
	* definição de DIps, scaling da camera de acordo...etc. 


10-05-2013
# refactorizações: 
	* desenho do texto e fontes revisitado
	* mecanismo das flashmessages
	
	* revisitado o mecanismo do ui que permite pegar nos objectos e criar o mousejoint
	* criado o LightSourceModel















------>> O mecanismo de pausa, como pára o render, também pára os efeitos das partículas. No entanto não pára o motor fisico.
------>> Podem ser os ScreenTickManager e o GameTickGenerator que orquestram o shutdown de tudo. P.e. podem lançar eventos de shutdown tal como há objectos com eventos de startup para quem os quer subscrever! 




# ver o problema da rotação: a rotação da estrelinha não está a 100%

# usar ruby para os scripts dos triggers (http://ruboto.org)
	# tb se podia usar python? (https://code.google.com/p/android-scripting/)
	# ou LUA (http://dotnetslackers.com/articles/mobile/Integrate-Lua-into-Your-Android-Games.aspx#s9-example-5-a-simple-game-case)

--> cada objecto subscreve o trigger e o script associado (com uma tag custom) ::
pe. trigger :: path_to_script



[TODO] 
# Gostava de implementar uma protecção de thread para garantir que determinado código só corre numa thread e outro código noutra.

# Construir o splashscreen!!
Assumir uma resolução e partir daí, com downscale/ upscale


# como faço o level design quando for um horizontal / vertical scroller ?

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