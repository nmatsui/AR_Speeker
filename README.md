AR_Speeker
==========

改良版が下記リポジトリにあります。Please see below repository.
https://github.com/tech-sketch/AR_Speaker

[AndAR](http://code.google.com/p/andar/)及びAndARModelViewerをベースに、以下の改造を加えています。

* 3Dモデルごとにマーカー画像を定義できるようにした
* 複数のマーカーを同時に認識できるようにした
* マーカー情報（の一部）をJava側から取得し、「マーカーの中心あたりをタップする」というイベントをハンドリングできるようにした

AndARはr205を想定しています。

またサンプルのモデルとして、[BLEND SWAP](http://www.blendswap.com/)にてCC0ライセンスで公開されている[CARTOON CHARACTER PACK 1](http://www.blendswap.com/blends/characters/cartoon-character-pack-1/)をwavefront形式に変換したものを利用しています。

注意
----

音声ファイルは別途準備してください。
res/rawに音声ファイルを配置し、リソースIDをPlayerの第四引数に与える必要があります

License
-------
Copyright(C) 2012 Nobuyuki Matsui (nobuyuki.matsui@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
