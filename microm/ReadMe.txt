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
# textureAtlas (v)
# tween accessor (v)

05-02-2013
# filtros sobre as texturas; conceitos: texel vs pixel, linear filter, nearest filter, mipmap(?), pixelperfect

07-02-2013
# mais umas investidas nos tipos de ecrãs e no cálculo dos parâmetros do CameraModel


[TODO] 
# como desenhar as texturas para os tamanhos? relação entre dimensões e resoluções

# counter de vidas e mecanismo de restart no mesmo nivel
# zona de texto do UI para contar as vidas/ saltar para o menu, etc.


# tutorial da relva (usa os tweens ?)
# particles na destruição

# efeito de paralax


[BUGS]
# no android o background não aparece... (parece-me que é dos POTs)

[ideias]
# sistema de niveis com percentagens e temas tipo o blast the monkey, ou o cut the ropee
# usar a facilidade de port do libgdx para o browser para fazer hosting de games (network) and bets and stuff