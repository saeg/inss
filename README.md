Instrumentation Strategies Simulator

===============================================================================

Para instalar no *unix e mac:

1: Execute o script do Ant para distribuir o projeto. (build.xml)
   ant dist
   
2: Ap�s a execu��o do script, na pasta 'build' deve ter todos os projetos compilados
   j2gxl, inss, opal, mis
   
   na pasta 'mis' t�m todos os arquivos necess�rios. 
   Copie esta pasta para o diretorio que desejar (e.g., /home/user/apps)

3: execute o comando 
   export MIS_HOME=/home/user/apps/mis
   
   Lembrando que o diret�rio deve corresponder ao diret�rio onde voc� copiou a pasta 'mis'

4: ativar a permiss�o de execu��o dos arquivos na pasta 'bin'
   chmod +x instrumenter alluses defuse requirements allusesextractor

5: Para facilitar o uso dos scripts adicione a pasta 'bin'ao PATH do sistema


Para instalar no Windows:

1: Execute o script do Ant para distribuir o projeto. (build.xml)
   ant dist
   
2: Ap�s a execu��o do script, na pasta 'build' deve ter todos os projetos compilados
   j2gxl, inss, opal, mis
   
   na pasta 'mis' t�m todos os arquivos necess�rios. 
   Copie esta pasta para o diretorio que desejar (e.g., C:\apps)

3: Clique com bot�o direito em 'My Computer' e v� em Manager
   ou pressione as teclas Windows + Pause
   
4: Na aba 'Advanced' clique em 'Envirenment Variables...'
   
5: Em 'user variables' clique em 'New...'
   Adicione os seguintes dados
     Variable Name: MIS_HOME
     Variable Value: C:\apps\mis

   Lembrando que o diret�rio deve corresponder ao diret�rio onde voc� copiou a pasta 'mis'

6: Para facilitar o uso dos scripts adicione a pasta 'bin'ao PATH do sistema

---

[![githalytics.com alpha](https://cruel-carlota.pagodabox.com/c3a7086e16115f7d3c7154d193aa7c25 "githalytics.com")](http://githalytics.com/saeg/inss)
