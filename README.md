Instrumentation Strategies Simulator

===============================================================================

Para instalar no *unix e mac:

1: Execute o script do Ant para distribuir o projeto. (build.xml)
   ant dist
   
2: Após a execução do script, na pasta 'build' deve ter todos os projetos compilados
   j2gxl, inss, opal, mis
   
   na pasta 'mis' têm todos os arquivos necessários. 
   Copie esta pasta para o diretorio que desejar (e.g., /home/user/apps)

3: execute o comando 
   export MIS_HOME=/home/user/apps/mis
   
   Lembrando que o diretório deve corresponder ao diretório onde você copiou a pasta 'mis'

4: ativar a permissão de execução dos arquivos na pasta 'bin'
   chmod +x instrumenter alluses defuse requirements allusesextractor

5: Para facilitar o uso dos scripts adicione a pasta 'bin'ao PATH do sistema


Para instalar no Windows:

1: Execute o script do Ant para distribuir o projeto. (build.xml)
   ant dist
   
2: Após a execução do script, na pasta 'build' deve ter todos os projetos compilados
   j2gxl, inss, opal, mis
   
   na pasta 'mis' têm todos os arquivos necessários. 
   Copie esta pasta para o diretorio que desejar (e.g., C:\apps)

3: Clique com botão direito em 'My Computer' e vá em Manager
   ou pressione as teclas Windows + Pause
   
4: Na aba 'Advanced' clique em 'Envirenment Variables...'
   
5: Em 'user variables' clique em 'New...'
   Adicione os seguintes dados
     Variable Name: MIS_HOME
     Variable Value: C:\apps\mis

   Lembrando que o diretório deve corresponder ao diretório onde você copiou a pasta 'mis'

6: Para facilitar o uso dos scripts adicione a pasta 'bin'ao PATH do sistema

---

[![githalytics.com alpha](https://cruel-carlota.pagodabox.com/c3a7086e16115f7d3c7154d193aa7c25 "githalytics.com")](http://githalytics.com/saeg/inss)
